class SessionsController < ApplicationController
  skip_before_action :ensure_login, only: [:new, :create]

  def new
  end

  def create
    uname = params["user"]["username"]
    password = params["user"]["password"]
    user = User.where(username: uname).first
    if not user.nil? and user.authenticate(password)
      session[:user_id] = user.id
      redirect_to "/", notice: "Logged in successfully"
    else
      redirect_to login_path, alert: "Log-in unsuccessful"
    end
  end

  def destroy
    reset_session
    redirect_to login_path, notice: "Logged out successfully" 
  end
end
