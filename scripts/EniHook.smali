.class public Lcom/eni/hook/EniHook;
.super Ljava/lang/Object;

.field public static final SERVICE_COMPONENT:Landroid/content/ComponentName;

.method static constructor <clinit>()V
    .registers 3

    new-instance v0, Landroid/content/ComponentName;

    const-string v1, "com.axlebolt.standoff2.huawei"

    const-string v2, "com.eni.hook.EniService"

    invoke-direct {v0, v1, v2}, Landroid/content/ComponentName;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    sput-object v0, Lcom/eni/hook/EniHook;->SERVICE_COMPONENT:Landroid/content/ComponentName;

    return-void
.end method

.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static start(Landroid/content/Context;)V
    .registers 4

    if-nez p0, :cond_a

    return-void

    :cond_a
    new-instance v0, Landroid/content/Intent;

    sget-object v1, Lcom/eni/hook/EniHook;->SERVICE_COMPONENT:Landroid/content/ComponentName;

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Landroid/content/ComponentName;)V

    const-string v1, "com.axlebolt.standoff2.huawei"

    invoke-virtual {v0, v1}, Landroid/content/Intent;->setPackage(Ljava/lang/String;)Landroid/content/Intent;

    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    return-void
.end method