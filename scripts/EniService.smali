.class public Lcom/eni/hook/EniService;
.super Landroid/app/Service;

.field private overlayView:Landroid/view/View;

.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Landroid/app/Service;-><init>()V

    return-void
.end method

.method public onBind(Landroid/content/Intent;)Landroid/os/IBinder;
    .registers 2

    const/4 v0, 0x0

    return-object v0
.end method

.method public onCreate()V
    .registers 1

    invoke-super {p0}, Landroid/app/Service;->onCreate()V

    invoke-virtual {p0}, Lcom/eni/hook/EniService;->startOverlay()V

    return-void
.end method

.method public onStartCommand(Landroid/content/Intent;II)I
    .registers 4

    invoke-virtual {p0}, Lcom/eni/hook/EniService;->startOverlay()V

    const/4 v0, 0x1

    return v0
.end method

.method public onDestroy()V
    .registers 4

    invoke-super {p0}, Landroid/app/Service;->onDestroy()V

    iget-object v0, p0, Lcom/eni/hook/EniService;->overlayView:Landroid/view/View;

    if-nez v0, :cond_skip

    const-string v1, "window"

    invoke-virtual {p0, v1}, Lcom/eni/hook/EniService;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/WindowManager;

    invoke-interface {v1, v0}, Landroid/view/WindowManager;->removeView(Landroid/view/View;)V

    :cond_skip
    return-void
.end method

.method public startOverlay()V
    .registers 8

    iget-object v0, p0, Lcom/eni/hook/EniService;->overlayView:Landroid/view/View;

    if-nez v0, :cond_return

    new-instance v0, Lcom/eni/hook/EniOverlayView;

    invoke-direct {v0, p0}, Lcom/eni/hook/EniOverlayView;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/eni/hook/EniService;->overlayView:Landroid/view/View;

    const-string v1, "window"

    invoke-virtual {p0, v1}, Lcom/eni/hook/EniService;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/view/WindowManager;

    # LayoutParams: MATCH_PARENT x MATCH_PARENT, TYPE_APPLICATION_OVERLAY = 0x7f6,
    # FLAG_NOT_FOCUSABLE(8)|FLAG_LAYOUT_IN_SCREEN(0x100)|FLAG_NOT_TOUCHABLE(0x10) = 0x118, TRANSLUCENT = -2
    new-instance v2, Landroid/view/WindowManager$LayoutParams;

    const/4 v3, -0x1

    const/4 v4, -0x1

    const/16 v5, 0x7f6

    const/16 v6, 0x118

    const/4 v7, -0x2

    invoke-direct/range {v2..v7}, Landroid/view/WindowManager$LayoutParams;-><init>(IIIII)V

    const/16 v3, 0x30

    iput v3, v2, Landroid/view/WindowManager$LayoutParams;->gravity:I

    invoke-interface {v1, v0, v2}, Landroid/view/WindowManager;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    :cond_return
    return-void
.end method