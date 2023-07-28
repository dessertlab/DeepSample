import pandas as pd
import sys

# print(sys.argv[1])
df = pd.read_csv("model"+sys.argv[1]+"_final.csv")

threshold_dsa = df["dsa"].mean() + 2*df["dsa"].std()

print(threshold_dsa)