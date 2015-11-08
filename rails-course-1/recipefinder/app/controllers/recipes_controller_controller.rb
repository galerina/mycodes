class RecipesControllerController < ApplicationController
  def index
    @response = nil
    if params[:search]
      @recipes = Recipe.for(params[:search])
    else
      @recipes = Recipe.for('chocolate')
    end
  end
end
