import subprocess
import os
# (1) cd into src dir
# (2) run:
#           /usr/local/bin/python3 eval.py
#

for alg in ['b', 'i']:
    for sample_size in [5000, 10000, 20000, 30000, 40000]:
        for rep in range(20):
            f_dir = f"../dat/{alg}/{sample_size}"
            f_path = f"{f_dir}/{rep}.txt"
            if not os.path.exists(f_dir):
                os.makedirs(f_dir)
            with open(f_path, 'w') as f:
                process = subprocess.Popen(
                    ['java',
                    'Main',
                    f"-{alg}",
                    f"{sample_size}",
                    f"../input/ca-AstroPh.txt"], 
                    stdout=f, 
                    universal_newlines=True
                )
                process.wait()
            print(".", end="", flush=True)
