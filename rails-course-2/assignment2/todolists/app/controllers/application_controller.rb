class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception
  helper_method :logged_in?, :current_user, :ensure_login
  before_action :ensure_login

  def logged_in?
    !session[:user_id].nil? && !current_user.nil?
  end

  def current_user
    User.where(id: session[:user_id]).first
  end

  def ensure_login
    if not logged_in? 
      redirect_to login_path
    end
  end
end
