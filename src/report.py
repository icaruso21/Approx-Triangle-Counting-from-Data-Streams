import matplotlib.pyplot as plt
import math
import numpy as np

# (1) cd into ./src dir
# (2) run:
#           /usr/local/bin/python3.9 report.py
#
runs = {'b': {5000: [], 10000: [], 20000: [], 30000: [], 40000: []}, 'i': {5000: [], 10000: [], 20000: [], 30000: [], 40000: []}}
sample_sizes = [5000, 10000, 20000, 30000, 40000]
algs = ['b', 'i']

for alg in algs:
    for sample_size in sample_sizes:
        for rep in range(20):
            f_dir = f"../dat/{alg}/{sample_size}"
            f_path = f"{f_dir}/{rep}.txt"
            
            with open(f_path, 'r') as f:
                lines = [int(line.rstrip()) for line in f]
                runs[alg][sample_size].append(lines)
            print(".", end="", flush=True)

for alg in algs:
    if alg == 'b':
        alg_name = 'Triest-Base'
    else:
        alg_name = 'Triest-Impr'

    for sample_size in sample_sizes:
        replicates = runs[alg][sample_size]
        time = range(len(replicates[0]))
        min_vals = []
        max_vals = []
        med_vals = []
        first_quar = []
        third_quar = []
        print(f"Processing alg: {alg} sample_size: {sample_size}")
        for i in time:
            vals_i = []
            for replicate in replicates:
                vals_i.append(replicate[i])
            vals_i_arr = np.array(vals_i)

            min_vals.append(np.min(vals_i))
            max_vals.append(np.max(vals_i))
            med_vals.append(np.percentile(vals_i_arr, 50))
            first_quar.append(np.percentile(vals_i_arr, 25))
            third_quar.append(np.percentile(vals_i_arr, 75))
        #Make plot (and save!)
        plt.plot(time, max_vals, linestyle = (0, (3, 10, 1, 10)), color = "red", label = "Max", linewidth = 1)
        plt.plot(time, min_vals, linestyle = (0, (3, 5, 1, 5)), color = "green", label = "Min", linewidth = 1)
        plt.plot(time, med_vals, linestyle = (0, (3, 5, 1, 5, 1, 5)), color = "orange", label = "Med", linewidth = 1)
        plt.plot(time, first_quar, linestyle = (0, (3, 1, 1, 1)), color = "purple", label = "Q1", linewidth= 1)
        plt.plot(time, third_quar, linestyle = (0, (3, 1, 1, 1, 1, 1)), color = "blue", label = "Q3", linewidth= 1)
        plt.xlabel("Time")
        plt.ylabel("Estimated Num. Triangles")
        plt.title(f"{alg_name} with {sample_size} Sample Size")
        plt.legend(loc=4)
        plt.savefig(f'../plots/{alg}_{sample_size}.png')
        #plt.show()
        plt.clf()
