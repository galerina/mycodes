# -*- coding: utf-8 -*-
class Recipe < ActiveRecord::Base
  include HTTParty

  hostport = ENV["FOOD2FORK_SERVER_AND_PORT"] || "www.food2fork.com"
  base_uri "http://#{hostport}/api"
  format :json
  default_params key: ENV["FOOD2FORK_KEY"]

  def Recipe.for(query_string)
    get("/search", {query: { q: query_string }})["recipes"]
  end
end
