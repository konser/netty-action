package com.plf.action.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.plf.action.entity.TranslatorData;
import com.plf.action.entity.TranslatorDataWapper;

import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {
	private String producerId;
	
	private RingBuffer<TranslatorDataWapper> ringBuffer;
	
	public MessageProducer(String producerId,RingBuffer<TranslatorDataWapper> ringBuffer) {
		this.setProducerId(producerId);
		this.ringBuffer = ringBuffer;
	}
	
	public void onData(TranslatorData data,ChannelHandlerContext ctx){
		long sequence = ringBuffer.next();
		try {
			TranslatorDataWapper wapper = ringBuffer.get(sequence);
			wapper.setData(data);
			wapper.setCtx(ctx);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

}
