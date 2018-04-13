package zsh.demos.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import zsh.demos.rabbitmq.produce.RBQProducer;
import zsh.demos.rabbitmq.produce.ZCCProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {
	
	@Autowired RBQProducer rbqProducer;
	@Autowired ZCCProducer zccProducer;

	@Test
	public void contextLoads() {
		
		for (int i=0; i< 100; i++) {
			rbqProducer.produce(String.valueOf(i));
//			zccProducer.produce();
		}
	}

}
