def ishiragana(c):
    return c >= 'ぁ' and c <= 'ん'

def iskatakana(c):
    return c >= 'ァ' and c <= 'ヶ'

def iskanji(c):
    return c >= '一' and c <= '黢'

def isother(c):
    return c == 'ー'

def is_japanese_text(s):
    for c in s:
        if not ishiragana(c) and not iskatakana(c) and not iskanji(c) and not isother(c):
            return False
    return True
