echo Running DeepSample ...

echo Running Classification ...
sh DeepSample/run_DeepSample_part1_class.sh
sh DeepSample/run_DeepEST.sh
sh DeepSample/run_SUPS.sh

echo Running Regression ...
sh DeepSample/run_DeepSample_part1_reg.sh
sh DeepSample/run_DeepEST_reg.sh
sh DeepSample/run_SUPS_reg.sh

echo COMPLETED