budget=50
model=Dave_orig
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv lsa $budget 5614 Results/Regression/SUPS/$model.lsa  >> log.txt
rm log.txt
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv VAE $budget 5614 Results/Regression/SUPS/$model.VAE >> log.txt
rm log.txt
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv SAE $budget 5614 Results/Regression/SUPS/$model.SAE >> log.txt
rm log.txt

model=Dave_dropout
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv lsa $budget 5614 Results/Regression/SUPS/$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv VAE $budget 5614 Results/Regression/SUPS/$model.VAE >> log.txt
rm log.txt
java -jar DeepSample/SUPS_reg.jar dataset/$model.csv SAE $budget 5614 Results/Regression/SUPS/$model.SAE >> log.txt
rm log.txt
