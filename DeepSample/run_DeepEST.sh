cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py A)
threshold_dsa_a=$(python3 threshold_dsa.py A)
cd ..

budget=50
model=A_final
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 60500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 60500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 60500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt

cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py B)
threshold_dsa_a=$(python3 threshold_dsa.py B)
cd ..

budget=50
model=B_final
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 60500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 60500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 60500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt



cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py C)
threshold_dsa_a=$(python3 threshold_dsa.py C)
cd ..

budget=50
model=C_final
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 60500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 60500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx51200m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 60500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt

cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py G)
threshold_dsa_a=$(python3 threshold_dsa.py G)
cd ..

budget=50
model=G_final
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 15000 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 15000 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 15000 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt

cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py H)
threshold_dsa_a=$(python3 threshold_dsa.py H)
cd ..

budget=50
model=H_final
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 15000 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 15000 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 15000 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt



cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py I)
threshold_dsa_a=$(python3 threshold_dsa.py I)
cd ..

budget=50
model=I_final
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 15000 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 15000 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 15000 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt

cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py D)
threshold_dsa_a=$(python3 threshold_dsa.py D)
cd ..

budget=50
model=D_final
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 33500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 33500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 33500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt

cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py E)
threshold_dsa_a=$(python3 threshold_dsa.py E)
cd ..

budget=50
model=E_final
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 33500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 33500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 33500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt



cd dataset
threshold_lsa_a=$(python3 threshold_lsa.py F)
threshold_dsa_a=$(python3 threshold_dsa.py F)
cd ..

budget=50
model=F_final
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv confidence 0.7 $budget 33500 Results/Classification/DeepEST/Model$model.confidence >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv lsa $threshold_lsa_a $budget 33500 Results/Classification/DeepEST/Model$model.lsa >> log.txt
rm log.txt
java -Xmx12288m -jar DeepSample/DeepEST_class.jar dataset/model$model.csv dsa $threshold_dsa_a $budget 33500 Results/Classification/DeepEST/Model$model.dsa >> log.txt
rm log.txt
