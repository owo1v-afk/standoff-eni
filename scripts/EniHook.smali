.class public Lcom/eni/hook/EniHook;
.super Ljava/lang/Object;

.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static start(Landroid/content/Context;)V
    .registers 7

    .catchall {:try_start_0 .. :try_end_0} :handler_0

    :try_start_0
    if-nez p0, :cond_skip

    return-void

    :cond_skip

    # EniOverlayView поверх окна игры
    new-instance v0, Lcom/eni/hook/EniOverlayView;

    invoke-direct {v0, p0}, Lcom/eni/hook/EniOverlayView;-><init>(Landroid/content/Context;)V

    # FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    new-instance v1, Landroid/widget/FrameLayout$LayoutParams;

    const/4 v2, -0x1

    const/4 v3, -0x1

    invoke-direct {v1, v2, v3}, Landroid/widget/FrameLayout$LayoutParams;-><init>(II)V

    # ((Activity)ctx).addContentView(view, params)
    check-cast p0, Landroid/app/Activity;

    invoke-virtual {p0, v0, v1}, Landroid/app/Activity;->addContentView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    :try_end_0

    return-void

    :handler_0
    move-exception v0

    return-void
.end method