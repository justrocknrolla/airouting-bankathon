import sys
import random
from itertools import izip

from config import PSP_LIST, BIN_LIST, REQUIRED_NUMBER_OF_ARGUMENTS, BIN_PSP_PAIRS


def generate_psp_bin_list():
    for psp in PSP_LIST:
        for bank_identification_number in BIN_LIST:
            yield (psp, bank_identification_number)


def parse_commandline():
    args = sys.argv
    if not REQUIRED_NUMBER_OF_ARGUMENTS == len(args):
        raise ValueError("improper number of arguments, must be {0}".format(REQUIRED_NUMBER_OF_ARGUMENTS))
    for i in range(0, BIN_PSP_PAIRS):
        actual_probability, count = args[2 * i + 1], args[2 * (i + 1)]
        yield (actual_probability, count)


def generate_historical_records_for_single_combinations(actual_probability, count):
    if actual_probability < 0 or actual_probability > 1:
        raise ValueError("actual probability must lie between 0 and 1")
    if count < 0:
        raise ValueError("requested number of records must be non-negative integer")
    for i in range(0, count):
        yield 0 if random.random() > actual_probability else 1


def generate_all_records(psp_bins, args):
    for psp_bin_pair, arg_pair in izip(psp_bins, args):
        psp, bank_identification_number = psp_bin_pair
        actual_probability, count = arg_pair
        for observation in generate_historical_records_for_single_combinations(float(actual_probability), int(count)):
            yield (psp, bank_identification_number, observation)


if __name__ == "__main__":
    psp_bins = generate_psp_bin_list()
    args = parse_commandline()
    records = generate_all_records(psp_bins, args)

    for record in records:
        psp, bank_identification_number, observation = record
        print("{0},{1},{2}".format(psp, bank_identification_number, observation))
