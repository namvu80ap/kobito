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
import org.apache.spark.mllib.linalg.Vectors;
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

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.data.cassandra.port=9042","spring.data.cassandra.contact-points=35.194.98.121",
		"spring.data.cassandra.keyspace-name=kobito_dev"})
public class KobitoAiApplicationTests {

	@Autowired
	TweetAnalystService tweetAnalystService;

	@Test
	public void testEstimator() throws Exception {
		SparkConf conf = new SparkConf().setAppName("App Local").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

		List<TradeTweet> listTradeTweet = tweetAnalystService.analystIncomeTweet("Sell USDJPY lost 10 pids");


		List<Row> data = listTradeTweet.stream().map(
				item -> RowFactory.create(Arrays.asList(item.getText().split(" ")))).collect(Collectors.toList());

//						new JavaLabeledDocument( new Long(item.getId()), item.getText(), 1.0 )).collect(Collectors.toList());

//		List<JavaLabeledDocument> lset = listTradeTweet.stream().map(
//				item -> new JavaLabeledDocument( new Long(item.getId()), item.getText(), 1.0 )).collect(Collectors.toList());

		StructType schema = new StructType(new StructField[]{
				new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
		});
		Dataset<Row> documentDF = spark.createDataFrame(data, schema);

		// Learn a mapping from words to Vectors.
		Word2Vec word2Vec = new Word2Vec()
				.setInputCol("text")
				.setOutputCol("result")
				.setVectorSize(10)
				.setMinCount(0);

		Word2VecModel model = word2Vec.fit(documentDF);
		Dataset<Row> result = model.transform(documentDF);

		LogisticRegression lr = new LogisticRegression()
				.setMaxIter(10)
				.setRegParam(0.001);

		lr.fit(result);

		StructType schema2 = new StructType(new StructField[]{
				new StructField("label", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
		});


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
