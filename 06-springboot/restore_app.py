# This script reads the backup from the conversation artifacts and writes App.jsx
# Since we don't have a backup, we'll reconstruct from the CSS file patterns

import os

# Read the Desafio.css to confirm it exists
css_path = r'c:\Users\charl\OneDrive\Documentos\GitHub\3035-TEACH\06-springboot\frontend\src\Desafio.css'
print(f"Desafio.css exists: {os.path.exists(css_path)}")

# Check if there's a cached version in node_modules/.vite
jsx_path = r'c:\Users\charl\OneDrive\Documentos\GitHub\3035-TEACH\06-springboot\frontend\src\App.jsx'
print(f"App.jsx size: {os.path.getsize(jsx_path) if os.path.exists(jsx_path) else 'MISSING'}")

# Check for any backup
for root, dirs, files in os.walk(r'c:\Users\charl\OneDrive\Documentos\GitHub\3035-TEACH\06-springboot\frontend'):
    for f in files:
        if 'App' in f and f.endswith(('.jsx', '.bak', '.orig')):
            fp = os.path.join(root, f)
            print(f"Found: {fp} ({os.path.getsize(fp)} bytes)")
    # Don't recurse into node_modules
    if 'node_modules' in dirs:
        dirs.remove('node_modules')
