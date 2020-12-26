import pandas as pd  # pandas
import numpy as np  # numpy
from imblearn.over_sampling import SMOTE  # Sample equalization algorithm
from sklearn.preprocessing import LabelEncoder  # Code conversion
from sklearn.model_selection import train_test_split, cross_val_score, StratifiedKFold  # Model processing method
from sklearn.linear_model import LogisticRegression  # Logistic regression
from sklearn import tree  # Import the decision tree module
from sklearn.ensemble import AdaBoostClassifier, VotingClassifier, GradientBoostingClassifier, \
    RandomForestClassifier, BaggingClassifier  # five integrated classification libraries and voting method libraries
import matplotlib.pyplot as plt  # gallery
import seaborn as sns  # Data visualization


# Data basic status view
def set_summary(df):
    print("Data preview:")
    print(df)
    print('{:*^30}'.format('Data type:'))
    print(df.dtypes)
    print('{:*^70}'.format('Data basic description statistics:'))
    print(df.describe().round(1).T)
    print('-' * 60)

# Data preprocessing
# Missing value review and processing
def na_summary(df):
    print("Missing value review and processing:")
    na_cols = df.isnull().any(axis=0)  # Check to see if each column has a missing value
    print('NA Cols:')
    print(na_cols)  # View columns with missing values
    print('-' * 30)
    na_lines = df.isnull().any(axis=1)  # See if each row has a missing value
    print('NA Recors:')
    print('Total number of NA lines is: {0}'.format(na_lines.sum()))  # View the total number of row records with missing values
    print('-' * 60)

# Calculate the correlation coefficient matrix
def corr_summary(df):
    corr = df.corr(method='pearson')  # pearson
    print('The correlation coefficient matrix is:\n', np.round(corr, 2))
    # Mapping of thermal diagram
    plt.rcParams['font.family'] = ['SimHei']
    plt.subplots(figsize=(10, 10))
    plt.title('Correlation thermal chart')
    sns.heatmap(corr, annot=True, vmax=1, square=True, cmap='Blues')
    plt.show()
    plt.close

# See if the data category has an exception data category
def type_summary(df):
    print('{:*^60}'.format('View the data grouping categories:'))
    for i in df.keys():
        df_group = raw_data.groupby(by=i)  # Use the groupBY method to group
        Cata_list = list(df_group.groups.keys())
        print('{0}：{1}'.format(i, Cata_list))

# Data preprocessing: string classification, integer classification
def label_encoder(df):
    conver_cols = ['GENDER', 'MARITAL_STATUS', 'PAY_METD', 'PAY_METD_PREV', 'CUSTOMER_TYPE']
    convert_matrix = df[conver_cols]  # Gets the array to convert
    for each_col in conver_cols:  # Convert separately for each column
        each_data = convert_matrix[each_col]  # Gets the data for each column
        model_le = LabelEncoder()  # Build model objects
        each_con_data = model_le.fit_transform(each_data)  # Direct conversion
        convert_matrix.loc[:, each_col] = each_con_data  # Replace the converted value with the original value
    print('{:*^60}'.format('View the spliced and transformed data:'))
    print(convert_matrix)
    return convert_matrix

# Class A sample equilibrium review
def label_samples_summary(df):
    print('{:*^60}'.format('Sample equilibrium examination:'))
    print(df.iloc[:, 0].groupby(df.iloc[:, -1]).count())  # Group by the data type in the last column

# Program entrance
dtypes = {'AGE': np.int64}  # The age as an integer
raw_data = pd.read_excel(r'C:\Users\LXJ\Desktop\Data analysis\typedata.xls', dtype=dtypes, sheet_name=0)  # Read the data in Table 1
predict_data = pd.read_excel(r'C:\Users\LXJ\Desktop\Data analysis\typedata.xls', dtype=dtypes, sheet_name=1)  # Read the data in Table 2

set_summary(raw_data)  # Basic status view
na_summary(raw_data)  # Missing value review
corr_summary(raw_data)  # Relevance review

# Delete abnormal data
raw_data = raw_data.drop(['NUM_ACT_TEL'], axis=1)  # As you can see from the correlation coefficient matrix, NUM_ACT_TEL is highly correlated with NUM_TEL, where NUM_ACT_TEL is discarded
raw_data = raw_data.drop_duplicates(keep='last', inplace=False)  # Discard duplicate data

type_summary(raw_data)  # Data category view

# Combining data basic description statistics and category processing outliers
raw_data = raw_data[raw_data['SUBPLAN_PREVIOUS'] <= 2248]  # Discard data from SUBPLAN_PREVIOUS > 2248
raw_data = raw_data[raw_data['NUM_TEL'] <= 26]  # Discard NUM_TEL > 26 data
print('{:*^60}'.format('View the data after deleting the exception data:'))
print(raw_data)  # Check the basic situation of the pre-processed training data, a total of 1,950 pieces of data

all_data = raw_data.append(predict_data)  # The training data and the prediction data were put together for the string classification and integer classification to avoid the later conversion
convert_matrix = label_encoder(all_data)  # Sort the merged data by string and integer

# dataframe horizontal data splicing
cols = ['AGE', 'CUSTOMER_CLASS', 'LINE_TENURE', 'SUBPLAN', 'SUBPLAN_PREVIOUS', 'NUM_TEL']  # Numeric type column
sacle_matrix = raw_data[cols]
train_data = pd.concat([sacle_matrix, convert_matrix.head(1950)], axis=1)  # It can be seen from the data printed above that there are 1950 lines after the pre-processing of training data, and the first 1950 lines are directly selected here

label_samples_summary(train_data)  # Class A sample equilibrium review

# The input data and forecast data are split and converted to NUMPY format
exam_X = train_data[['AGE', 'CUSTOMER_CLASS', 'LINE_TENURE', 'SUBPLAN', 'SUBPLAN_PREVIOUS', 'NUM_TEL', 'GENDER',
                     'MARITAL_STATUS', 'PAY_METD', 'PAY_METD_PREV']]
exam_y = train_data['CUSTOMER_TYPE']
exam_X = np.array(exam_X)
exam_y = np.array(exam_y)

# Sample balance
model_smote = SMOTE()  # Build the object of SMOTE model
exam_X, exam_y = model_smote.fit_sample(exam_X, exam_y)  # Input data were sampled

train_X, test_X, train_y, test_y = train_test_split(exam_X, exam_y, train_size=0.8)  # Split the training data and test data
print('{:*^30}'.format('View the number of training set and test set data:'))
print('The training set data size is', train_X.size, train_y.size)
print('The test set data size is', test_X.size, test_y.size)

# Classification model establishment
model_lr = LogisticRegression(max_iter=1000)  # Logical forest model objects
model_dt = tree.DecisionTreeClassifier()  # Decision tree model object
model_rf = RandomForestClassifier(n_estimators=15, random_state=0)  # Random forest model object
model_adac = AdaBoostClassifier(random_state=0)  # Adaboost classification model object
model_bagc = BaggingClassifier(n_estimators=15, random_state=0)  # Bagging classification model object
model_gdbc = GradientBoostingClassifier(max_features=0.8, random_state=0)  # GradientBoosting classification model object

# Review the classification model's score against the test data one by one
print('{:*^60}'.format('Test scores of the six categories:'))
model_list = [model_lr, model_dt, model_rf, model_adac, model_bagc, model_gdbc]
for i in model_list:
    i.fit(train_X, train_y)
    print(i.score(test_X, test_y))

estimators = [('decisiontree', model_dt), ('randomforest', model_rf),
              ('adaboost', model_adac),
              ('bagging', model_bagc), ('gradientboosting', model_gdbc)]  # Create a list of portfolio evaluators

model_vot = VotingClassifier(estimators=estimators, voting='soft',
                             weights=[1, 1.2, 0.9, 1.2, 1], n_jobs=-1)  # Set up the portfolio evaluation model and set the appropriate weight

cv = StratifiedKFold(5)  # Set up a cross check method
cv_score = cross_val_score(model_vot, train_X, train_y, cv=cv)
print('{:*^60}'.format('Cross validation scores for voting classifier models:'))
print(cv_score)  # Print each cross check score
print('Average score: %.2f' % cv_score.mean())  # Print the average cross test score

# The new data set makes the prediction
convert_matrix2 = convert_matrix.tail(3).iloc[:, :-1]  # Take the tabbed data from the first three lines to the last and remove the last column
# Horizontal splicing prediction data
sacle_matrix2 = predict_data[cols]
predict_data = pd.concat([sacle_matrix2, convert_matrix2], axis=1)
predict_data = np.array(predict_data)  # Dataframe is always converted to numpy when training

model_vot.fit(train_X, train_y)  # Voting classifier model training
y_predict = model_vot.predict(predict_data)  # Predicted results
print('{:*^60}'.format('Predicted results:'))
print(y_predict)  # Print predicted value