# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

import sys
import cbor2

if len(sys.argv) != 3:
    print("Usage: python generate_vm_data_simple.py <input_string> <output_file_path>")
    sys.exit(1)

input_string = sys.argv[1]
output_file_path = sys.argv[2]

data = {}
for pair in input_string.split(','):
    key, value = pair.split(':', 1)
    try:
        if '.' in value:
            value = float(value)
        else:
            value = int(value)
    except ValueError:
        pass  # Keep as string
    data[key] = value

with open(output_file_path, "wb") as f:
    cbor2.dump(data, f)


