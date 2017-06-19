package com.org.kobito.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

@SpringBootApplication
public class KobitoAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KobitoAiApplication.class, args);
	}
}
