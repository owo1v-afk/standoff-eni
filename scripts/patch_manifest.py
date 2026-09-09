#!/usr/bin/env python3
import sys

path = sys.argv[1]
with open(path) as f:
    src = f.read()

if "com.eni.hook.EniService" in src:
    print("already patched")
    sys.exit(0)

perm = '<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>\n    '
if 'android.permission.SYSTEM_ALERT_WINDOW' not in src:
    anchor = 'android.permis'
    idx = src.index('<uses-permission android:name="android.permission.') if False else None

# вставка разрешения после открывающего <manifest ...>
if 'android.permission.SYSTEM_ALERT_WINDOW' not in src:
    i = src.index('android:supportsRtl')
    # ставим перед <application>, проще: прямо перед первым <application>
    a = src.index('<application')
    src = src[:a] + perm + src[a:]

# служба внутри <application>
svc = '        <service android:exported="false" android:name="com.eni.hook.EniService"/>\n        '
a = src.index('<application')
i = src.index('>', a) + 1
src = src[:i] + '\n' + svc + src[i:]

with open(path, "w") as f:
    f.write(src)
print("patched")