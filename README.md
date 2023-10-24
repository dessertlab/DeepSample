# DeepSample

This repository provides the artifacts for DeepSample execution. 
The experiments on 9 DNN for classification and 2 DNN for regression can be replicated by running the script 'run_DeepSample.sh'.
Once the experiments are completed, all the results are generated in the 'Results' folder.

## DeepSample folder
The folder "DeepSample" contains all the ".jar" files and the source code.
The source code is organized as follows:
- The "main" folder contains all the java files for the jar generation.
- "selector.classification" contains the source code of the implemented sampling algorithms for classification, plus DeepEST.
- "selector.regression" contains the source code of the implemented sampling algorithms for regression, plus DeepEST.
- "utility" and "utility.regression" contains utilities and data structures useful for the selectors.
The code has been developed with Eclipse.

## Other samplers
CES and SRS implementations are available at Li et al. repo 'https://github.com/Lizenan1995/DNNOpAcc'.

## Neural Networks availability
The trained models are included in the 'models' folder.
The folder 'dataset' contains the dataset with the predictions and the auxiliary variables for all models.
The source code for the classification models is available at Guerriero et al. repo: 'https://github.com/ICOS-OAA/ICOS.git'

## Paper results
The folder 'Results_paper' contains the results reported in the paper.

## Dependencies
The "libs" folder contains all the libraries required to run the experiments.