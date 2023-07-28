import pandas as pd
import sys

df = pd.read_csv("model"+sys.argv[1]+"_final.csv")

threshold_lsa = df["lsa"].mean() + df["lsa"].var()

print(threshold_lsa)