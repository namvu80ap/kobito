package com.org.kobito.ai;

//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
import com.org.kobito.ai.model.TradeTweet;
import com.org.kobito.ai.services.TweetAnalystService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.feature.*;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.apache.spark.sql.functions.col;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.data.cassandra.port=9042","spring.data.cassandra.contact-points=104.198.95.15",
		"spring.data.cassandra.keyspace-name=kobito_dev"})
public class KobitoAiApplicationTests {

	@Autowired
	TweetAnalystService tweetAnalystService;

//	@Test
	public void LSHSimiliar() {
		SparkConf conf = new SparkConf().setAppName("App Local").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

		// $example on$
		List<Row> dataA = Arrays.asList(
				RowFactory.create(0, Vectors.dense(1.0, 1.0)),
				RowFactory.create(1, Vectors.dense(1.0, -1.0)),
				RowFactory.create(2, Vectors.dense(-1.0, -1.0)),
				RowFactory.create(3, Vectors.dense(-1.0, 1.0))
		);

		List<Row> dataB = Arrays.asList(
				RowFactory.create(4, Vectors.dense(1.0, 0.0)),
				RowFactory.create(5, Vectors.dense(-1.0, 0.0)),
				RowFactory.create(6, Vectors.dense(0.0, 1.0)),
				RowFactory.create(7, Vectors.dense(0.0, -1.0))
		);

		StructType schema = new StructType(new StructField[]{
				new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
				new StructField("features", new VectorUDT(), false, Metadata.empty())
		});
		Dataset<Row> dfA = spark.createDataFrame(dataA, schema);
		Dataset<Row> dfB = spark.createDataFrame(dataB, schema);

		org.apache.spark.ml.linalg.Vector key = Vectors.dense(0.0, 0.0);

		BucketedRandomProjectionLSH mh = new BucketedRandomProjectionLSH()
				.setBucketLength(2.0)
				.setNumHashTables(3)
				.setInputCol("features")
				.setOutputCol("hashes");

		BucketedRandomProjectionLSHModel model = mh.fit(dfA);

		// Feature Transformation
		System.out.println("The hashed dataset where hashed values are stored in the column 'hashes':");
		model.transform(dfA).show();

		// Compute the locality sensitive hashes for the input rows, then perform approximate
		// similarity join.
		// We could avoid computing hashes by passing in the already-transformed dataset, e.g.
		// `model.approxSimilarityJoin(transformedA, transformedB, 1.5)`
		System.out.println("Approximately joining dfA and dfB on distance smaller than 1.5:");
		model.approxSimilarityJoin(dfA, dfB, 1.5, "EuclideanDistance")
				.select(col("datasetA.id").alias("idA"),
						col("datasetB.id").alias("idB"),
						col("EuclideanDistance")).show();

		// Compute the locality sensitive hashes for the input rows, then perform approximate nearest
		// neighbor search.
		// We could avoid computing hashes by passing in the already-transformed dataset, e.g.
		// `model.approxNearestNeighbors(transformedA, key, 2)`
		System.out.println("Approximately searching dfA for 2 nearest neighbors of the key:");
		model.approxNearestNeighbors(dfA, key, 10).show();
		// $example off$

		spark.stop();
	}

	@Test
	public void testEstimator() throws Exception {
		SparkConf conf = new SparkConf().setAppName("App Local").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

		List<TradeTweet> listTradeTweet = tweetAnalystService.analystIncomeTweet("Sell USDJPY lost 10 pips");

//		List<Row> data = listTradeTweet.stream().map(
//				item -> {
//					String text = item.getText();
//					if( text.contains("Sell") || text.contains("pips") )
//						return RowFactory.create(1.0, Arrays.asList(text.split(" ")));
//					else
//						return RowFactory.create(0.0, Arrays.asList(text.split(" ")));
//				}
//		).collect(Collectors.toList());

		// Prepare test documents, which are unlabeled.
		List<Row> testList = Arrays.asList(
				RowFactory.create( 0.0, Arrays.asList("Forex Sell loss 10 pips".split(" ")) ),
				RowFactory.create( 0.0, Arrays.asList("Tam bay".split( " "))),
				RowFactory.create( 0.0, Arrays.asList("Bought USDJPY 1.22".split(" ")) ),
				RowFactory.create( 0.0, Arrays.asList("Anh yeu em".split(" "))),
				RowFactory.create( 0.0, Arrays.asList("MAPS Closed #FX #Forex #USDCADgcm Sell 1.2932 from 1.1232 for 12.0 pips".split(" "))),
				RowFactory.create( 0.0, Arrays.asList("Deposit Bonus Award From Eqtrades https://t.co/rQMhwr0mgK #FOREX #BONUS #PROMOTION".split(" ")))
		);

//						new JavaLabeledDocument( new Long(item.getId()), item.getText(), 1.0 )).collect(Collectors.toList());

//		List<JavaLabeledDocument> lset = listTradeTweet.stream().map(
//				item -> new JavaLabeledDocument( new Long(item.getId()), item.getText(), 1.0 )).collect(Collectors.toList());

		StructType schema = new StructType(new StructField[]{
				new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
				new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
		});

		Dataset<Row> documentDF = spark.createDataFrame(data, schema);

		Dataset<Row> testDF = spark.createDataFrame(testList, schema);

		// Learn a mapping from words to Vectors.
		Word2Vec word2Vec = new Word2Vec()
				.setInputCol("text")
				.setOutputCol("features")
				.setVectorSize(10)
				.setMinCount(0);

		Word2VecModel model = word2Vec.fit(documentDF);

//		model.findSynonymsArray();
		Dataset<Row> result = model.transform(documentDF);

		Word2VecModel model2 = word2Vec.fit(testDF);
		Dataset<Row> r2 = model2.transform(testDF);
		LinearRegression lg = new LinearRegression();
		LinearRegressionModel lgModle = lg.fit(result);
		lgModle.transform(r2).show(10);

//		LogisticRegression lr = new LogisticRegression()
//				.setMaxIter(10)
//				.setRegParam(0.01);
////
//		LogisticRegressionModel lrModel = lr.fit(result);
////
//		Dataset<Row> restul2 = lrModel.transform(r2);
//		restul2.show(100);

//		result.show();
//
//		List<Row> newText = Arrays.asList(
////				RowFactory.create( 0.0, Arrays.asList("I love you more than i can say".split(" ")) ),
//				RowFactory.create( 0.0, Arrays.asList("Sing a song".split(" ")) ),
//				RowFactory.create( 0.0, Arrays.asList("a m j dh".split(" ")) ),
////				RowFactory.create( 0.0, Arrays.asList("okane ga agaru".split(" ")) ),
////				RowFactory.create( 0.0, Arrays.asList("I go to buy a bag, then I will sell my car".split(" ")) ),
////				RowFactory.create( 0.0, Arrays.asList("Money will trade tonight".split(" ")) ),
//				RowFactory.create( 0.0, Arrays.asList("Forex want to buy but we have to".split( " ")))
//		);
//
//		Dataset<Row> newDF = spark.createDataFrame(newText, schema);
//		Dataset<Row> newResult = model.transform(newDF);
//
//		StructType schema2 = new StructType(new StructField[]{
//				new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
//				new StructField("features", new VectorUDT(), false, Metadata.empty())
//		});
//
//		JavaRDD<Row> rowRDD = result.javaRDD().map((Function<Row, Row>) record -> {
//			return RowFactory.create(record.get(0),record.get(2));
//		});
//
//
//		JavaRDD<Row> newTest = newResult.javaRDD().map((Function<Row, Row>) record -> {
//			return RowFactory.create(record.get(0),record.get(2));
//		});
//
//		Dataset<Row> logicDF = spark.createDataFrame(rowRDD,schema2);
//
//		Dataset<Row> testDF  = spark.createDataFrame(newTest,schema2);
//
//		LogisticRegression lr = new LogisticRegression()
//				.setMaxIter(10)
//				.setRegParam(0.01);
////
//		LogisticRegressionModel model2 = lr.fit(result);
////
//		Dataset<Row> restul2 = model2.transform(result);
//		restul2.show(100);
//


//		for (Row row : result.collectAsList()) {
//			//List<String> text = row.getList(0);
//			DenseVector vector = (DenseVector) row.get(1);
//			System.out.println("Text: " + vector + "\n");
//		}

//		List<Row> data = listTradeTweet.map(
//				RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
//				RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
//				RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
//		);


//		Tokenizer tokenizer = new Tokenizer()
//				.setInputCol("text")
//				.setOutputCol("words");
//		HashingTF hashingTF = new HashingTF()
//				.setNumFeatures(1000)
//				.setInputCol(tokenizer.getOutputCol())
//				.setOutputCol("features");
//		LogisticRegression lr = new LogisticRegression()
//				.setMaxIter(10)
//				.setRegParam(0.001);
//
//		Pipeline pipeline = new Pipeline()
//				.setStages(new PipelineStage[] {tokenizer, hashingTF, lr});
//
//		//JavaRDD<TradeTweet> list = sc. parallelize(listTradeTweet);
//
//		Dataset<Row> df = spark.createDataFrame(lset, JavaLabeledDocument.class);
//
//		Word2Vec word = new Word2Vec();
//		word.fit(df);
//		LogisticRegressionModel model = lr.fit(df);

//		PipelineModel model = pipeline.fit(df);

		// Prepare test documents, which are unlabeled.
//		Dataset<Row> test = spark.createDataFrame(Arrays.asList(
//				new JavaDocument(4L, "Sell USDJPY lost 10 pids"),
//				new JavaDocument(5L, "Lam gi bay gio"),
//				new JavaDocument(6L, "we sell in a lot of USD forex"),
//				new JavaDocument(7L, "predict the forex price is good to buy USDJPY"),
//				new JavaDocument(8L, "p wj hd k")
//		), JavaDocument.class);
//
//		Dataset<Row> predictions = model.transform(test);
//		for (Row r : predictions.select("id", "text", "probability", "prediction").collectAsList()) {
//			System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> prob=" + r.get(2)
//					+ ", prediction=" + r.get(3));
//		}


	}


}
