import sys

filepath = r'c:\Users\charl\OneDrive\Documentos\GitHub\3035-TEACH\06-springboot\frontend\src\App.jsx'

with open(filepath, 'rb') as f:
    raw = f.read()

text = raw.decode('utf-8')
lines = text.splitlines(keepends=True)
fixed_lines = []

for i, line in enumerate(lines):
    try:
        fixed = line.encode('latin-1').decode('utf-8')
        fixed_lines.append(fixed)
    except (UnicodeDecodeError, UnicodeEncodeError):
        fixed_lines.append(line)

result = ''.join(fixed_lines)

with open(filepath, 'wb') as f:
    f.write(result.encode('utf-8'))

# Verify
with open(filepath, 'r', encoding='utf-8') as f:
    vlines = f.readlines()
print(f'Line 25: {vlines[24].strip()}')
print(f'Line 88: {vlines[87].strip()}')
print(f'Line 104: {vlines[103].strip()}')
print(f'Line 684: {vlines[683].strip()}')
print(f'Total lines: {len(vlines)}')
print('Done!')
