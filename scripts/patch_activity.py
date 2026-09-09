#!/usr/bin/env python3
import sys

path = sys.argv[1]
with open(path) as f:
    src = f.read()

needle = "    invoke-super {p0, p1}, Lcom/unity3d/player/UnityPlayerActivity;->onCreate(Landroid/os/Bundle;)V"
inject = "\n    # ENI: автозапуск чита\n    invoke-static {p0}, Lcom/eni/hook/EniHook;->start(Landroid/content/Context;)V"

if "EniHook;->start" in src:
    print("already patched")
    sys.exit(0)

if needle not in src:
    print("needle not found")
    sys.exit(1)

target = src.index("return-void", src.index(needle))
src = src[:target] + inject.strip() + "\n\n" + src[target:]
with open(path, "w") as f:
    f.write(src)
print("patched")