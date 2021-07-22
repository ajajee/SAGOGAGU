package com.wannabe.be.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wannabe.be.event.dao.EventMapper;

@Service
@Transactional
public class EventServiceImpl implements EventService{
	
	@Autowired
	EventMapper eventMapper;
	
}
