from pathlib import Path
import re

errors = []
count = 0
for build_file in sorted(Path('src').glob('*/*/build.gradle.kts')):
    text = build_file.read_text(encoding='utf-8')
    if not re.search(r'contentWarning\s*=\s*ContentWarning\.SAFE', text):
        errors.append(f'{build_file}: source must explicitly declare ContentWarning.SAFE')
    else:
        count += 1
    if build_file.parts[1] != 'ar':
        errors.append(f'{build_file}: only Arabic sources are permitted in this repository')
if errors:
    print('\n'.join(errors))
    raise SystemExit(1)
print(f'Validated {count} explicitly SAFE Arabic source extensions.')
