# SMGTask
Me just being bored  

Just clone the repo and use docker-compose.yaml to start kafka and elastic search.  
Start the application from intellj inside the test folder there is a test class named   
SendMessages inside message producer folder with three tests.  
    @Test  
    public void  sendCreateKafkaMessage()   
    @Test  
    public void sendUpdateKafkaMessage()  
    @Test  
    public void sendDeleteKafkaMessage()   
You can use first test to send kafka message to create a random car and look up id inside the logs.  
After that fire the request http://localhost:8080/search/someId with POSTMAN and you should get a normal resposne.  
You can then fire the updateKafkaMessage (just copy id) and check again through postman that it works.  
And lastly you can fire delete and after that you should get 404.  

By using pageable we can have something like this  
http://localhost:8080/search/?pageId=1&size=20  

Everything else is in the code ;D  


  