package com.org.kobito.ai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tensorflow.*;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class KobitoAiApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		try (Graph g = new Graph()) {
			final String value = "Hello from " + TensorFlow.version();

			// Construct the computation graph with a single operation, a constant
			// named "MyConst" with a value "value".
			try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
				// The Java API doesn't yet include convenience functions for adding operations.
				g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
			}

			// Execute the "MyConst" operation in a Session.
			try (Session s = new Session(g);
				 Tensor output = s.runner().fetch("MyConst").run().get(0)) {
				System.out.println(new String(output.bytesValue(), "UTF-8"));
			}
		}

	}

}
