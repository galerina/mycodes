class Profile < ActiveRecord::Base
  belongs_to :user

  include ActiveModel::Validations
  validate :first_name_or_last_name_non_nil
  validate :gender_is_binary
  validate :no_boys_named_sue

  def first_name_or_last_name_non_nil
    if first_name.nil? and last_name.nil?
      errors.add(:first_name, "first name and last name can not both be nil")
      errors.add(:last_name, "first name and last name can not both be nil")
    end
  end

  def gender_is_binary
    if gender != "male" and gender != "female"
      errors.add(:gender, "gender must be 'male' or 'female'")
    end
  end

  def no_boys_named_sue
    if gender == "male" and first_name == "Sue"
      errors.add(:first_name, "a boy can't be named 'Sue'")
    end
  end

  def Profile.get_all_profiles(min, max)
    Profile.all.where("birth_year BETWEEN ? and ?", min, max).order(:birth_year)
  end
end
