#!/bin/bash

amount=$1

# Define a list of names
names=("John" "Jane" "Mary" "James" "Emily" "Michael" "Sarah" "Jessica" "Jacob" "Mohamed" "Emma" "Daniel" "Olivia" "David" "Sophia" "Lucas" "Mia" "Matthew" "Charlotte" "Ethan")

output_dir="src/data"

# Output file
output_file="people_2.csv"

mkdir -p "$output_dir"

# Remove output file if it exists
if [ -f "$output_dir/$output_file" ]; then
    rm "$output_dir/$output_file"
fi

# Generate entries
for ((i=0; i<$amount; i++))
do
    # Generate a random name and age
    name="${names[$RANDOM % ${#names[@]}]} ${chars:$RANDOM%${#chars}:10}"
    age=$i

    # Write to the output file
    echo "$name,$age" >> "$output_dir/$output_file"
done



