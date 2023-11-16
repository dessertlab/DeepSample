'''

'''
import numpy as np
import keras.backend as K
from collections import defaultdict
from tensorflow.keras.utils import to_categorical
np.random.seed(1)
CLIP_MIN = -0.5
CLIP_MAX = 0.5

LAY = -2 #H & I -4 #G -2

def build_neuron_tables(model, x_test, divide):
    total_num = x_test.shape[0]
    # init dict and its input
    neuron_interval = defaultdict(np.array)
    neuron_proba = defaultdict(np.array)
    input_tensor = model.input
    layer = model.layers[LAY]
    print(model.layers[LAY])

    output = test_output
    lower_bound = np.min(output, axis=0)
    upper_bound = np.max(output, axis=0)

    for index in range(output.shape[-1]):
        # compute interval
        # temp = (upper_bound[index] - lower_bound[index]) * .25
        # let interval = 30
        interval = np.linspace(
            lower_bound[index], upper_bound[index], divide)
        neuron_interval[(layer.name, index)] = interval
        neuron_proba[(layer.name, index)] = output_to_interval(
            output[:, index], interval) / total_num

    return neuron_interval, neuron_proba


def build_testoutput(model, x_test):
    input_tensor = model.input
    layer = model.layers[LAY]
    # get this layer's output
    output = layer.output
    output_fun = K.function([input_tensor], [output])
    output = output_fun([x_test])[0]
    output = output.reshape(output.shape[0], -1)
    test_output = output
    return test_output


def neuron_entropy(model, neuron_interval, neuron_proba, sample_index):
    total_num = sample_index.shape[0]
    if(total_num == 0):
        return -1e3
    neuron_entropy = []
    layer = model.layers[LAY]
    output = test_output
    output = output[sample_index, :]
    # get lower and upper bound of neuron output
    # lower_bound = np.min(output, axis=0)
    # upper_bound = np.max(output, axis=0)
    for index in range(output.shape[-1]):
        # compute interval
        interval = neuron_interval[(layer.name, index)]
        bench_proba = neuron_proba[(layer.name, index)]
        test_proba = output_to_interval(
            output[:, index], interval) / total_num

        # cross entropy
        # test_proba = np.clip(test_proba, 1e-10, 1 - 1e-10)
        # log_proba = np.log(test_proba)
        # temp_proba = bench_proba.copy()
        # temp_proba[temp_proba < (.5 / total_num)] = 0
        # entropy = np.sum(log_proba * temp_proba)

        # KL divergence
        temp_proba1 = np.clip(test_proba, 1e-10, 1)
        temp_proba2 = np.clip(bench_proba, 1e-10, 1)
        entropy = np.sum(np.log(temp_proba2) * test_proba) - \
            np.sum(np.log(temp_proba1) * test_proba)

        neuron_entropy.append(entropy)
    return np.array(neuron_entropy)


def coverage(neuron_entropy):
    return np.mean(neuron_entropy)


def output_to_interval(output, interval):
    num = []
    for i in range(interval.shape[0] - 1):
        num.append(np.sum(np.logical_and(
            output > interval[i], output < interval[i + 1])))
    return np.array(num)


def selectsample(model, x_test, y_test, delta, iterate):
    test = x_test
    batch = delta
    arr = np.random.permutation(test.shape[0])
    max_index0 = arr[0:30]
    min_index0 = arr[0:30]
    # min_index0 = arr[-30:]

    acc_list1 = []
    acc_list2 = []
    cov_random = []
    cov_select = []
    for i in range(iterate):
        arr = np.random.permutation(test.shape[0])
        max_coverage = -1e3
        min_coverage = 0
        max_index = -1
        min_index = -1
        max_iter = 30

        e = neuron_entropy(model, neuron_interval,
                           neuron_proba, max_index0)
        cov = coverage(e)
        max_coverage = cov

        temp_cov = []
        index_list = []
        
        # select
        for j in range(max_iter):
            arr = np.random.permutation(test.shape[0])
            start = int(np.random.uniform(0, test.shape[0] - batch))
            temp_index = np.append(max_index0, arr[start:start + batch])
            index_list.append(arr[start:start + batch])
            e = neuron_entropy(model, neuron_interval,
                               neuron_proba, temp_index)
            new_coverage = coverage(e)
            temp_cov.append(new_coverage)

        max_coverage = np.max(temp_cov)
        cov_index = np.argmax(temp_cov)
        max_index = index_list[cov_index]
        if(max_coverage <= cov):
            start = int(np.random.uniform(0, test.shape[0] - batch))
            max_index = arr[start:start + batch]
        
        # random
        arr = np.random.permutation(test.shape[0])
        start = int(np.random.uniform(0, test.shape[0] - batch))
        min_index = arr[start:start + batch]

        max_index0 = np.append(max_index0, max_index)
        min_index0 = np.append(min_index0, min_index)

        #
        e = neuron_entropy(model, neuron_interval,
                           neuron_proba, max_index0)
        cov1 = coverage(e)

        e = neuron_entropy(model, neuron_interval,
                           neuron_proba, min_index0)
        cov2 = coverage(e)


        cov_select.append(cov1)
        cov_random.append(cov2)
        print("current select coverage is {!s}".format(cov1))
        print("current random coverage is {!s}".format(cov2))
        label = y_test[max_index0]
        label_c = []
        for i in range(label.shape[0]):
            label_c.append(label[i][0])
        orig_sample = x_test[max_index0]
        # orig_sample = orig_sample.reshape(-1, 28, 28, 1)
        pred = np.argmax(model.predict(orig_sample), axis=1)
        acc1 = np.sum(pred == label_c) / orig_sample.shape[0]
        print("select: failures: {!s} | total: {!s}".format(np.sum(pred != label_c),orig_sample.shape[0]))
        acc_list1.append(acc1)
        label = y_test[min_index0]
        label_c = []
        for i in range(label.shape[0]):
            label_c.append(label[i][0])
        orig_sample = x_test[min_index0]
        # orig_sample = orig_sample.reshape(-1, 28, 28, 1)
        pred = np.argmax(model.predict(orig_sample), axis=1)
        acc2 = np.sum(pred == label_c) / orig_sample.shape[0]
        print("random: failures: {!s} | total: {!s}".format(np.sum(pred != label_c),orig_sample.shape[0]))
        acc_list2.append(acc2)
        print("numuber of samples is {!s}, select acc is {!s}, random acc is {!s}".format(
            orig_sample.shape[0], acc1, acc2))
    return acc_list1, acc_list2, cov_select, cov_random


def experiments(delta, iterate):
    pred = np.argmax(model.predict(x_test), axis=1)
    y_test_c = []
    for i in range(x_test.shape[0]):
        y_test_c.append(y_test[i][0])

    #print(pred)
    #print(y_test_c)

    true_acc = np.sum(pred==y_test_c) / x_test.shape[0]
    print(true_acc)

    acc_list1, acc_list2, cov1, cov2 = selectsample(
        model, x_test, y_test, delta, iterate)
    print("the select std is {!s}".format(
        np.mean(np.abs(acc_list1 - true_acc))))
    print("the random std is {!s}".format(
        np.mean(np.abs(acc_list2 - true_acc))))

    return np.array(acc_list1), np.array(acc_list2), np.array(cov1), np.array(cov2)


if __name__ == '__main__':
    # preprocess the data set
    from keras.datasets import cifar100
    from sklearn.model_selection import train_test_split

    train = 40000 
    test = 15000
    val = 5000

    (x_train, y_train), (x_test, y_test) = cifar100.load_data(label_mode="fine")
    print('x_train shape:', x_train.shape)
    print(x_train.shape[0], 'train samples')
    print(x_test.shape[0], 'test samples')

    x = np.concatenate((x_train, x_test))
    y = np.concatenate((y_train, y_test))

    x_train, Xtemp, y_train, ytemp = train_test_split(x, y, train_size=train, random_state=0)

    x_val_test, x_test, y_val_test, y_test = train_test_split(Xtemp, ytemp, train_size=val, random_state=0)

    x_train = x_train.astype('float32')
    x_test = x_test.astype('float32')
    x_train /= 255
    x_test /= 255

    y_train = to_categorical(y_train, 100)
    y_test_cat = to_categorical(y_test, 100)

    from tensorflow import keras
    from tensorflow.keras.models import load_model
    model = load_model("../models/modelH.h5")
    print(model.summary())
    
    global test_output, neuron_interval, neuron_proba
    test_output = build_testoutput(model, x_test)
    neuron_interval, neuron_proba = build_neuron_tables(
        model, x_test, divide=30)

    for k in range(30):
        print("the {} exp".format(k))
        acc1, acc2, cov1, cov2 = experiments(delta=5, iterate=314)#154)#34)
        np.savetxt('data/select{}.csv'.format(k), acc1)
        # np.savetxt('data/random{}.csv'.format(k), acc2)
        # np.savetxt('data/select_cov{}.csv'.format(k), cov1)
        # np.savetxt('data/random_cov{}.csv'.format(k), cov2)
        # mat[:, 2 * k] = acc1
        # mat[:, 2 * k + 1] = acc2
    # np.savetxt("exp.csv", mat)
