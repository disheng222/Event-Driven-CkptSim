#This is a discrete-event driven simulator, which aims to simulate the job executions protected by a checkpointing/restart model, in the presence of possible failures occuring with different failure rates. 

This simulator has been used in our research team to verify the effectiveness of the proactive checkpointing method supported by an efficient failiure predictor algorithm: called Time Machine. 

For more details about this fault tolerance research, please check this paper: 

Khalid Ayed Alharthi, Arshad Jhumka, SHeng Di, Lin Gui, Franck Cappello, and Simon McIntosh-Smith, "Time Machine: Generative Real-Time Model For Failure (and Lead Time) Prediction in HPC Systems", in DSN2023. 

The execution entry function (main function) is in Simulation.java (for traditional fixed-length checkpointing/restart) or Simulation2.java (for proactive+fixed-length checkpointing/restart model facilitated by Time Machine algorithm)
The key parameters (such as checkpoint cost) are hard-coded in these two source files.
More parameters are hard-coded in the Job.java and Job2.java.
