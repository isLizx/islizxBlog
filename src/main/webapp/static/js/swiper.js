const swiper = {
    _parent:null,
    _parentW:null,
    _imgArr:null,
    _callBack:null,
    _clickEvent:null,
    _config:null,
    _swiperContent:null,
    _swiperPoint:null,
    _arrowLeft:null,
    _arrowRight:null,
    _timerFn:null,
    _itemArr:[],
    _pointArr:[],
    _currentPage:0,
    _num:null,
    _time:3000,
    _movePos:0,
    _canMove:false,
    _startX:null,
    _endX:null,
    _prevLeft:null,
    _nextLeft:null,
    _touchItem:null,
    _autoPlay:true,
    _isMobile:false,
    _showPoint:true,
    _startTime:null,
    _endTime:null,
    _prevFlag:true,
    _nextFlag:true,
    init:function(parent,imgArr,config,clickEvent,callBack){
        this._parent = parent;
        this._parentW = parent.width();
        this._imgArr = imgArr;
        this._config = config;
        this._callBack = callBack;
        this._clickEvent = clickEvent;
        this._currentPage = 0;
        this._time = 3000;
        this._itemArr = [];
        this._pointArr = [];
        this._num = imgArr ? imgArr.length : 0;
        this._isMobile =  (typeof this._config.ismobile === 'boolean') ? this._config.ismobile : this.isMobile();
        this._autoPlay = (typeof this._config.autoplay === 'boolean') ? this._config.autoplay : true;
        this._showPoint = (typeof this._config.showpoint === 'boolean') ? this._config.showpoint : true;
        this._canMove = false;
        this.addEle();
    },
    /**判断是否是移动端 */
    isMobile : function(){  
        let userAgentInfo = navigator.userAgent;//获取游览器请求的用户代理头的值
        let Agents = ["Android", "iPhone","SymbianOS", "Windows Phone","iPad","iPod"];//定义移动设备数组
        let isMobile = false;
        for (let v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {    
                isMobile = true;
                break;
            }
        }
        return isMobile;
    },
    /**填充元素 */
    addEle:function(){
        if(this._num <= 0)return;
        this._swiperContent = this._parent.children(".swiper-content");
        this._swiperPoint = this._parent.children(".swiper-point");
        this._imgArr.map((item,index)=>{
            let slidePictureUrl;
            let slideTitle;
            let slideUrl;
            let slideTarget;
            let targetType = '';
            if(typeof item === 'object'){
                slidePictureUrl = item.slidePictureUrl;
                slideTitle = item.slideTitle;
                slideUrl = item.slideUrl;
                slideTarget = item.slideTarget;
            }else{
                slidePictureUrl = item;
            }
            if(slideTarget == '新窗口'){
                targetType = '_blank';
            }
            let swiperItem = $('<div class="swiper-item"><a href="' + slideUrl + '" target="' + targetType + '"><img src="' + slidePictureUrl + '"></a><a href="' + slideUrl + '" target="' + targetType + '" class="ui teal button" style="border-radius: unset;position: absolute;top: 0;left: 0">' + slideTitle + '</a></div>');
            /*let swiperTitle = $('<p class="ui teal button" style="border-radius: unset;position: absolute;top: 0;left: 0">' + slideTitle + '</p>');*/
            /*swiperTitle.appendTo(this._parent);*/
            swiperItem.appendTo(this._swiperContent);
            /*if(typeof item === 'object'){
                for (let key in item){
                    swiperItem.prop(key,item[key]);
                }
            }*/
            this._itemArr.push(swiperItem);
            let pointItem = $(`<div class="swiper-circle"></div>`);
            pointItem.appendTo(this._swiperPoint);
            this._pointArr.push(pointItem);
            if(index == 0){
                swiperItem.css('left', '0');
                pointItem.addClass("current-point");
            }else{
                swiperItem.css('left', '100%');
                pointItem.removeClass("current-point");
            }
        })
        this.arrow();
        this.autoPlay();
        this.addTouch();
        this.showPoint();
        this._callBack && this._callBack(this._itemArr);
    },
    /**判断是都显示轮播点 */
    showPoint:function(){
        if(!this._showPoint){
            this._parent.children('.swiper-point').hide();
        }
    },
    /**判断是否显示左右按钮 */
    arrow:function(){
        let that = this;
        if(this._config.arrowtype == 'move'){
            this._parent.children('.arrow-left').hide();
            this._parent.children('.arrow-right').hide();
            this._parent.on('mouseenter', function(){
                that._parent.children('.arrow-left').fadeIn();
            });
            this._parent.on('mouseleave', function(){
                that._parent.children('.arrow-left').fadeOut();
            });
            this._parent.on('mouseenter', function(){
                that._parent.children('.arrow-right').fadeIn();
            });
            this._parent.on('mouseleave', function(){
                that._parent.children('.arrow-right').fadeOut();
            });
        }else if(this._config.arrowtype == 'none'){
            this._parent.children('.arrow-left').hide();
            this._parent.children('.arrow-right').hide();
        }
        this.addArrowListener();
    },
    addArrowListener:function(){
        let that = this;
        this._arrowRight = this._parent.children(".arrow-right");
        this._arrowRight.click(function(){
            that.next();
        })
        this._arrowLeft = this._parent.children(".arrow-left");
        this._arrowLeft.click(function(){
            that.prev();
        }); 
    },
    /**判断是否自动轮播*/
    autoPlay:function(){
        if(this._autoPlay){
            let that = this;
            this._time =  this._config.time || 3000;
            this.createTimer();
            this._parent.on('mouseover', function(){
                clearInterval(that._timerFn);
                that._timerFn = null;
            });
            this._parent.on('mouseout', function(){
                that.createTimer();
            });
        }
     },
     createTimer:function(){
        let that = this;
        this._timerFn = setInterval(function(){
            that.next();
        }, this._time);
     },
     /**转到下一个 */
     next:function(){
        if(this._num <= 1) return;
        this._itemArr[this._currentPage].animate({'left': "-100%"},300);
        this._pointArr[this._currentPage].removeClass("current-point");
        this._currentPage ++;
        if(this._currentPage > this._num - 1) this._currentPage = 0;
        this._itemArr[this._currentPage].css('left', '100%').show().animate({'left':0},300);
        this._pointArr[this._currentPage].addClass("current-point");
        this.addTouch();
     },
     /**转到上一个*/
     prev:function(){
        if(this._num <= 1) return;
        this._itemArr[this._currentPage].animate({'left': "100%"},300);
        this._pointArr[this._currentPage].removeClass("current-point");
        this._currentPage --;
        if(this._currentPage < 0) this._currentPage = this._num -1;
        this._itemArr[this._currentPage].css('left', '-100%').show().animate({'left':0},300);
        this._pointArr[this._currentPage].addClass("current-point");
        this.addTouch();
     },
     downFn:function(e){
        e.preventDefault();
        this._startTime = new Date().getTime();
        this._movePos = 0;
        this._canMove = true;
        this._startX = this._isMobile ? e.originalEvent.targetTouches[0].pageX : e.pageX;
        this._itemArr[this._currentPage].css('left',"0");
        if(this._num > 2){
            if(this._currentPage == 0){
                this._itemArr[this._num - 1].css('left',"-100%");
                this._itemArr[this._currentPage +1].css('left',"100%");
                this._prevLeft = this._itemArr[this._num - 1].position().left;
                this._nextLeft =  this._itemArr[this._currentPage +1].position().left;
            }else if(this._currentPage == this._num - 1){
                this._itemArr[this._currentPage - 1].css('left',"-100%");
                this._itemArr[0].css('left',"100%");
                this._prevLeft = this._itemArr[this._currentPage - 1].position().left;
                this._nextLeft =  this._itemArr[0].position().left;
            }else{
                this._itemArr[this._currentPage - 1].css('left',"-100%");
                this._itemArr[this._currentPage +1].css('left',"100%");
                this._prevLeft = this._itemArr[this._currentPage - 1].position().left;
                this._nextLeft =  this._itemArr[this._currentPage +1].position().left;
            }
        }
        if(this._isMobile){
            this._touchItem.on('touchmove',this.moveFn.bind(this));
            $(document).on('touchend',this.upFn.bind(this));
        }else{
            this._touchItem.on('mousemove',this.moveFn.bind(this));
            $(document).on('mouseup',this.upFn.bind(this));
        }
     },
     moveFn:function(e){
        if(this._canMove){
            if(this._isMobile && this._autoPlay && this._timerFn != null){
                clearInterval(this._timerFn);
                this._timerFn = null;
            }
            this._endX = this._isMobile ? e.originalEvent.targetTouches[0].pageX : e.pageX;
            this._movePos = this._endX - this._startX;
            this._itemArr[this._currentPage].animate({'left':this._movePos},0);
            if(this._num > 2){
                if(this._currentPage == 0){
                    this._itemArr[this._num - 1].animate({'left':this._prevLeft + this._movePos},0);
                    this._itemArr[this._currentPage +1].animate({'left':this._nextLeft + this._movePos},0);
                }else if(this._currentPage == this._num - 1){
                    this._itemArr[this._currentPage - 1].animate({'left':this._prevLeft + this._movePos},0);
                    this._itemArr[0].animate({'left':this._nextLeft + this._movePos},0);
                }else{
                    this._itemArr[this._currentPage - 1].animate({'left':this._prevLeft + this._movePos},0);
                    this._itemArr[this._currentPage +1].animate({'left':this._nextLeft + this._movePos},0);
                }
            }else if(this._num == 2){
                let index = this._currentPage == 0 ? 1 : 0;
                if(this._movePos > 0){
                    if(!this._nextFlag) this._nextFlag = true;
                    if(this._prevFlag){
                        this._prevFlag = false;
                        this._itemArr[index].css('left','-100%');
                        this._prevLeft = this._itemArr[index].position().left;
                    }
                    this._itemArr[index].animate({'left':this._prevLeft + this._movePos},0);
                }else{
                    if(!this._prevFlag) this._prevFlag = true;
                    if(this._nextFlag){
                        this._nextFlag = false;
                        this._itemArr[index].css('left','100%');
                        this._nextLeft = this._itemArr[index].position().left;
                    }
                    this._itemArr[index].animate({'left':this._nextLeft + this._movePos},0);
                }
            }
        }
     },
     upFn(e){
        if(!this._canMove) return;
        this._endTime = new Date().getTime();
        if(this._endTime - this._startTime < 200){
            this._clickEvent &&  this._clickEvent(this._touchItem); 
        }
        if(this._isMobile){
            this._touchItem.off('touchstart');
            this._touchItem.off('touchmove');
            $(document).off('touchend');
        }else{
            this._touchItem.off('mousedown');
            this._touchItem.off('mousemove');
            $(document).off('mouseup');
        }
        if(this._isMobile && this._autoPlay && this._timerFn == null){
            this.createTimer();
        }
        if(this._canMove) this._canMove = false;
        if(this._num > 2){
            if(Math.abs(this._movePos) > this._parentW / 3){
                if(this._movePos > 0){
                    this._itemArr[this._currentPage].animate({'left':'100%'},300);
                    if(this._currentPage == 0){
                        this._itemArr[this._num - 1].animate({'left':0},300);
                        this._itemArr[this._currentPage +1].css('left','100%');
                    }else if(this._currentPage == this._num - 1){
                        this._itemArr[this._currentPage - 1].animate({'left':0},300);
                        this._itemArr[0].css('left','100%');
                    }else{
                        this._itemArr[this._currentPage - 1].animate({'left':0},300);
                        this._itemArr[this._currentPage +1].css('left','100%');
                    }
                    this._pointArr[this._currentPage].removeClass("current-point");
                    this._currentPage --;
                    if(this._currentPage < 0) this._currentPage = this._num -1;
                    this._pointArr[this._currentPage].addClass("current-point");
                }else{
                    this._itemArr[this._currentPage].animate({'left':'-100%'},300);
                    if(this._currentPage == 0){
                        this._itemArr[this._num - 1].css('left','-100%');
                        this._itemArr[this._currentPage +1].animate({'left':0},300);
                    }else if(this._currentPage == this._num - 1){
                        this._itemArr[this._currentPage - 1].css('left','-100%');
                        this._itemArr[0].animate({'left':0},300);
                    }else{
                        this._itemArr[this._currentPage - 1].css('left','-100%');
                        this._itemArr[this._currentPage + 1].animate({'left':0},300);
                    }
                    this._pointArr[this._currentPage].removeClass("current-point");
                    this._currentPage ++;
                    if(this._currentPage > this._num - 1) this._currentPage = 0;
                    this._pointArr[this._currentPage].addClass("current-point");
                }
            }else{
                this._itemArr[this._currentPage].animate({'left':0},300);
                if(this._currentPage == 0){
                    this._itemArr[this._num - 1].animate({'left':'-100%'},300);
                    this._itemArr[this._currentPage +1].animate({'left':'100%'},300);
                }else if(this._currentPage == this._num - 1){
                    this._itemArr[this._currentPage - 1].animate({'left':'-100%'},300);
                    this._itemArr[0].animate({'left':'100%'},300);
                }else{
                    this._itemArr[this._currentPage - 1].animate({'left':'-100%'},300);
                    this._itemArr[this._currentPage + 1].animate({'left':'100%'},300);
                }
            }
        }else if(this._num == 2){
            let index = this._currentPage == 0 ? 1 : 0;
            if(Math.abs(this._movePos) > this._parentW / 3){
                if(this._movePos > 0){
                    this._itemArr[this._currentPage].animate({'left':'100%'},300);
                }else{
                    this._itemArr[this._currentPage].animate({'left':'-100%'},300);
                }
                this._itemArr[index].animate({'left':0},300);
                this._pointArr[this._currentPage].removeClass("current-point");
                this._pointArr[index].addClass("current-point");
                this._currentPage = index;
            }else{
                this._itemArr[this._currentPage].animate({'left':0},300);
                if(this._movePos > 0){
                    this._itemArr[index].animate({'left':'-100%'},300);
                }else{
                    this._itemArr[index].animate({'left':'100%'},300);
                }
            }
        }else if(this._num == 1){
            this._itemArr[this._currentPage].animate({'left':0},300);
        }
        this.addTouch();
     },
     /** 添加触摸事件*/
     addTouch:function(){
        let cantouch = (typeof this._config.cantouch === 'boolean') ? this._config.cantouch : true;
        if(cantouch){
            this._touchItem = this._itemArr[this._currentPage];
            if(this._isMobile){
                this._touchItem.on('touchstart',this.downFn.bind(this));
            }else{
                this._touchItem.on('mousedown',this.downFn.bind(this));
            }
        }
     }
}

