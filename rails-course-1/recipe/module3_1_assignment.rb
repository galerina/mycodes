require 'httparty'

class Recipe
  include HTTParty

  base_uri 'http://food2fork.com/api'
  format :json
  default_params key: ENV["FOOD2FORK_KEY"]

  def Recipe.for(query_string)
    get("/search", {query: { q: query_string }})["recipes"]
  end


end
