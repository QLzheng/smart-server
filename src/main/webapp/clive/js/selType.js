var speed=1;
var step = 500;
var hight = 103;
//0:up 1:down
var movdirect = 0;
var colee2=document.getElementById("colee2");
var colee1=document.getElementById("colee1");
var colee=document.getElementById("colee");
var MyMar;//=setInterval(Marquee1,speed)//设置定时器
colee2.innerHTML=colee1.innerHTML; //克隆colee1为colee2
var foc = 1;
//0 静止 1 动画中
var status = 0;
var focMax = 6;

function Marquee(){
	if(1==movdirect)
	{
		var to = 0;
		if(0 == foc)
		{
			to = focMax*hight;
		}
		else
		{
			to = foc*hight;
		}
		if(colee.scrollTop<=to)
		{
			colee.scrollTop+=step;
			if(colee.scrollTop>=to)
			{
				colee.scrollTop = to;
				if(colee.scrollTop >= focMax*hight)
				{
					colee.scrollTop = 0;
				}
				clearInterval(MyMar);
				status = 0;
				setTabFocusbg(foc+1,3);
			}
			
		}
	}
	else
	{
		if(colee.scrollTop <= 0)
		{
			colee.scrollTop = focMax*hight;
		}
		
		if(colee.scrollTop>(foc)*hight)
		{
			colee.scrollTop-=step;
			if(colee.scrollTop<=(foc)*hight)
			{
				colee.scrollTop = (foc)*hight;
				clearInterval(MyMar);
				status = 0;
				setTabFocusbg(foc+1,3);
			}
		}
	}
}


function fastFoc(){
	if(1==movdirect)
	{
		var to = 0;
		if(0 == foc)
		{
			to = focMax*hight;
		}
		else
		{
			to = foc*hight;
		}
		colee.scrollTop = to;
		if(colee.scrollTop >= focMax*hight)
		{
			colee.scrollTop = 0;
		}
		status = 0;
		setTabFocusbg(foc+1,3);
	}
	else
	{
		if(colee.scrollTop <= 0)
		{
			colee.scrollTop = focMax*hight;
		}
		
		colee.scrollTop = (foc)*hight;
		status = 0;
		setTabFocusbg(foc+1,3);
	}
}

function movup()
{
	foc--;
	if(foc<0)
	{
		foc = focMax-1;
	}
	movdirect = 0;	 	
	setFocus(foc);
}

function movdown()
{
	foc++;
	if(foc>=focMax)
	{
		foc = 0;
	}
	movdirect = 1;
	setFocus(foc);
}

function movefoc(){
	
}


function setFocus(fo)
{
	/*move down*/
	foc = fo;
	clrTabFocusbg(focMax);
	clearInterval(MyMar);
	status = 1;
	MyMar=setInterval(Marquee,speed);
}

function g(o){
	return document.getElementById(o);
}

function setTabFocusbg(n,counter){
	g('tb_'+n).className='cliveitemon_'+n;
	
	if(6==n)
	{
		g('favor_num').className = "favor_num_on";
		g('icon_'+n).className = "icon_on_"+n;
	}
	else
	{
		g('favor_num').className = "favor_num";
		g('icon_'+n).className = "icon_on";
	}
}

function setFavorNum(num)
{
	g('favor_num').innerHTML = num;
}

function clrTabFocusbg(counter){
	for(var i=1;i<=counter;i++){
		g('icon_'+i).className = "icon_"+i;
		g('tb_'+i).className='cliveitem_'+i;
	}
	g('favor_num').className = "favor_num";
}

// type : 类型 1 2 3， direct : 0 up 1 down
function setTabFocus(type,direct)
{
	movdirect = direct;
	if(1==status)
	{
		fastFoc();
	}
	setFocus(type-1);
}
function uiInit()
{
	setTabFocusbg(1,6);
}