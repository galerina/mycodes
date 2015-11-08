# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
User.destroy_all
Profile.destroy_all
TodoList.destroy_all
TodoItem.destroy_all

a_year_from_now = Date.today+1.year

profiles = Profile.create! [
  {gender: "female", birth_year: 1954, first_name: "Carly",  last_name: "Fiorina"},
  {gender: "male",   birth_year: 1946, first_name: "Donald", last_name: "Trump"},
  {gender: "male",   birth_year: 1951, first_name: "Ben",    last_name: "Carson"},
  {gender: "female", birth_year: 1947, first_name: "Hillary",last_name: "Clinton"}
]

profiles.each do |profile|
  u = User.create! username: profile.last_name, password_digest: "pass", profile: profile

  list = TodoList.create! list_name: "campaign", list_due_date: a_year_from_now, user: u
  5.times do |x|
    TodoItem.create! due_date: a_year_from_now, title: "TODO #{x}", description: "do something special", completed: false, todo_list: list
  end
end
