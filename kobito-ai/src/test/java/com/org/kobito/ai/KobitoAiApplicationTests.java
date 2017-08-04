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
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.data.cassandra.port=9042","spring.data.cassandra.contact-points=35.194.118.36",
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

		JavaRDD<TradeTweet> tradeTweetRDD = sc.parallelize(listTradeTweet);


		LinearRegression lr = new LinearRegression()
				.setMaxIter(10)
				.setRegParam(0.3)
				.setElasticNetParam(0.8);



		Dataset<Row>  dataFrame = spark.createDataFrame(tradeTweetRDD, TradeTweet.class);

		dataFrame.show();

//		LinearRegression lr = new LinearRegression();
//		LinearRegressionModel model = lr.train(dataFrame);
//
//
//		List<String> jsonData = Arrays.asList(
//				"{\"label\":\"90.0\",\"features\": \"close USDJPY -1 pids\"}");
//		Dataset<String> testData = spark.createDataset(jsonData, Encoders.STRING());
//		Dataset<Row> anotherTweet = spark.read().json(testData);
//
//		Dataset<Row> anotherTransform = model.transform(anotherTweet);

		//Dataset<Row> predict  = anotherTransform.select("realTradingEval","tweetText","prediction");
		//predict.show();

//		LogisticRegression lr = new LogisticRegression()
//				.setMaxIter(10)
//				.setRegParam(0.3)
//				.setElasticNetParam(0.8);
//
//		LogisticRegressionModel lrModel = lr.fit(tradeTweetRDD);


//		assertThat(listTradeTweet.size()).isGreaterThan(99);
	}

}
