#Implement all parts of this assignment within (this) module2_assignment2.rb file

class LineAnalyzer
  def initialize(content, line_number)
    @content = content
    @line_number = line_number
    calculate_word_frequency
  end

  def calculate_word_frequency
    words = content.split()
    words_histogram = Hash.new(0)
    words.each do |word|
      words_histogram[word.downcase] += 1
    end

    @highest_wf_count = words_histogram.values.max
    @highest_wf_words = words_histogram.keys.select do |item|
      words_histogram[item] == @highest_wf_count
    end
  end

  def highest_wf_count
    @highest_wf_count
  end

  def highest_wf_words
    @highest_wf_words
  end

  def content
    @content
  end

  def line_number
    @line_number
  end
end

class Solution
  def initialize()
    @analyzers = []
  end

  def analyze_file()
    @analyzers = File.readlines('test.txt').each_with_index.map do |content, line_number|
      LineAnalyzer.new(content, line_number)
    end
  end

  def calculate_line_with_highest_frequency()
    @highest_count_across_lines = @analyzers.map do |a|
      a.highest_wf_count
    end.max
    @highest_count_words_across_lines = @analyzers.select do |a|
      a.highest_wf_count == @highest_count_across_lines
    end
  end

  def print_highest_word_frequency_across_lines()
    p "The following words have the highest word frequency per line:"
    @highest_count_words_across_lines.each do |a|
      p "#{a.highest_wf_words} (appears in line #{a.line_number})"
    end
  end

  def analyzers
    @analyzers
  end

  def highest_count_across_lines
    @highest_count_across_lines
  end

  def highest_count_words_across_lines
    @highest_count_words_across_lines
  end
end
