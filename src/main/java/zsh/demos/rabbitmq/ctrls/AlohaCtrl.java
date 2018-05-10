package zsh.demos.rabbitmq.ctrls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zsh.demos.rabbitmq.produce.RBQProducer;
import zsh.demos.rabbitmq.produce.ZCCProducer;

@RestController
public class AlohaCtrl {
	
	@Autowired private RBQProducer rbqProducer;
	@Autowired private ZCCProducer zccProducer;
	
//	@AutoLog
	@GetMapping("/")
	public String aloha(@RequestParam(value="ipaddr", required=false) String ipaddr) {
		rbqProducer.produce("HI");
//		zccProducer.produce("HI");
		return "Aloha!";
	}

}
