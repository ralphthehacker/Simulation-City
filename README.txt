After a long discussion, we decided to build our model from the ground up in Java.  We'd rather have the satisfaction of doing everything scratch than building on top of a robust piece of software.  

Checkpoint 1
------------
For this week, we intend to create the basic classes and software hierarchy.  Lawrence will get the ball rolling by making an outline of the classes, and Ralph and Sal will flesh those classes out and add on with the necessary methods and supporting classes.

Checkpoint 2
------------
For this week, we planned to have a fully running simulation. We finished the businesses, hiring and firing employees, looking for new jobs, and the state machine representation. The simulation is now functional. However, we only did superficial testing. We will spend next week testing the app and thinking about how to visualize the data.


- Make transitions realistic in the state machine. Add relevancy parameter with higher priority over th

- Making a person connect to the Groceries/Businesses/Entertainment through the SHOP.
 Consider current state

- Add more variability to places. Make people spend their cash in difference things. Make their choices state based

-Residences: Buying stuff and moving around. Ensuring people move around.

- Ensuring that two Businesses/Home/Grocery/Entertainment don't stay in the same place

- Visualization: Start with a simple print-like walkthrough. People moving from position X to position Y, doing Z. Also print the attributes: how much money and current parameters.

Checkpoint 3
------------
The model is now almost fully functional.  Significant work has been done in the Person, Business, StateTransition, and Simulation class.  
The work was mainly tuning the parameters while creating and connecting methods to support normal human behavior.
In particular, the state machine now provides a reasonable representation of how people would act based on their needs and time of day.  
The visualization of the simulation (though still terminal based) is now also more robust, with several print statements added to show the flow of human activity.  
A new class called GlassdoorDotCom was added to replicate the process of finding a job for the citizens.  
Businesses can also hire and fire based on the performance of their employees. 
Shops and residences have been added and will shortly be fully connected.  

