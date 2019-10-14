import flask, pymongo
from flask import Flask, render_template, request, redirect, url_for


app = Flask(__name__)
# myclient = pymongo.MongoClient("mongodb://localhost:27017/")  # connection url
# print("connected to mongo")
# mydb = myclient['medicare'] # connection to db
# print("connected to db")


@app.route("/", methods=["GET"])
def root():
    print("In root")
    return render_template('home.html')

@app.route("/login", methods=["GET", "POST"])
def login():
    print("login")
    return render_template('login.html')

@app.route("/register", methods=["GET", "POST"])
def register():
    print("register")
    if request.form["cancel"]:
        redirect(url_for('root'))
    return render_template("register.html")