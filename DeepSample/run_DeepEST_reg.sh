budget=50
model=Dave_orig
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv lsa $budget 5614 Results/Regression/DeepEST/$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv VAE $budget 5614 Results/Regression/DeepEST/$model.VAE >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv SAE $budget 5614 Results/Regression/DeepEST/$model.SAE >> log.txt
rm log.txt

model=Dave_dropout
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv lsa $budget 5614 Results/Regression/DeepEST/$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv VAE $budget 5614 Results/Regression/DeepEST/$model.VAE >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_reg.jar dataset/$model.csv SAE $budget 5614 Results/Regression/DeepEST/$model.SAE >> log.txt
rm log.txt
