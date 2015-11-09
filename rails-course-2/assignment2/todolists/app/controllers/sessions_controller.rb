class SessionsController < ApplicationController
  def new
  end

  def create
    uname = params["user"]["username"]
    password = params["user"]["password"]
    user = User.where(username: uname).first
    if not user.nil? and user.authenticate(password)
      session[:user_id] = user.id
      redirect_to root_path, notice: "Logged in successfully"
    else
      redirect_to login_path, alert: "Log-in unsuccessful"
    end
  end

  def destroy
    reset_session
    redirect_to login_path, notice: "Logged out successfully" 
  end
end
