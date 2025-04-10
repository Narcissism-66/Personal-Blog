/*
 Navicat Premium Data Transfer

 Source Server         : 枫梓
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : localhost:3306
 Source Schema         : test_01

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 10/04/2025 15:29:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `authorId` int NOT NULL,
  `updateDate` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `watches` int NOT NULL DEFAULT 0,
  `favourites` int UNSIGNED NULL DEFAULT 0,
  `likes` int NULL DEFAULT 0,
  `isPublic` tinyint NULL DEFAULT 0,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (22, '数的范围（二分）', 1, '2025-04-06 13:00:40', 19, 1, 1, 0, '**题目：**\n给定一个按照升序排列的长度为 n 的整数数组，以及 q 个查询。\n对于每个查询，返回一个元素 k 的起始位置和终止位置（位置从 0 开始计数）。\n如果数组中不存在该元素，则返回 -1 -1。\n\n**输入格式**\n第一行包含整数 n 和 q ，表示数组长度和询问个数。\n\n第二行包含 n 个整数（均在 1∼10000 范围内），表示完整数组。\n\n接下来 q 行，每行包含一个整数 k，表示一个询问元素。\n\n**输出格式**\n共 q 行，每行包含两个整数，表示所求元素的起始位置和终止位置。\n如果数组中不存在该元素，则返回 -1 -1。\n\n**数据范围**\n```language\n1≤n≤100000\n\n1≤q≤10000\n\n1≤k≤10000\n```\n\n\n**输入样例：**\n```language\n6 3\n1 2 2 3 3 4\n3\n4\n5\n```\n\n**输出样例：**\n```language\n3 4\n5 5\n-1 -1\n```\n***代码***\n```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=1e5+10;\nll n,q;\nll a[N];\nint main()\n{\n    cin>>n>>q;\n    for(ll i=0;i<n;i++)cin>>a[i];\n    while(q--)\n    {\n        ll x;\n        cin>>x;\n        ll l=0,r=n-1;\n        while(l<r)\n        {\n            ll mid=l+r>>1;\n            if(a[mid]<x)l=mid+1;\n            else r=mid;\n        }\n       if(a[l]!=x)\n       {\n           cout<<\"-1 -1\"<<endl;\n           continue;\n       }\n       else \n       {\n           cout<<l<<\" \";\n       }\n        l=0,r=n-1;\n        while(l<r)\n        {\n            ll mid=(l+r+1)>>1;\n            if(a[mid]<=x)l=mid;\n            else r=mid-1;\n        }\n        cout<<l<<endl;\n    }\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (23, '离散化（算法）', 1, '2024-11-23 15:21:45', 6, 1, 1, 0, '**题目：**\n假定有一个无限长的数轴，数轴上每个坐标上的数都是 0。\n\n现在，我们首先进行 n 次操作，每次操作将某一位置 x 上的数加 c。\n\n接下来，进行 m 次询问，每个询问包含两个整数 l 和 r，你需要求出在区间 [l,r] 之间的所有数的和。\n**输入格式**\n第一行包含两个整数 n 和 m。\n\n接下来 n 行，每行包含两个整数 x 和 c。\n\n再接下来 m 行，每行包含两个整数 l 和 r。\n\n**输出格式**\n共 m 行，每行输出一个询问中所求的区间内数字和。\n\n**数据范围**\n```language\n−109≤x≤109\n\n1≤n,m≤105\n\n−109≤l≤r≤109\n\n−10000≤c≤10000\n```\n\n\n**输入样例：**\n```language\n3 3\n1 2\n3 6\n7 5\n1 3\n4 6\n7 8\n```\n\n**输出样例：**\n```\n8\n0\n5\n```\n***代码***\n```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=3e5+10;\nll s[N],a[N];\nvector<int> alls;//下标\nvector<pair<int,int>> add,query;//add存储值及坐标，query存储要询问的坐标\nint find(ll x){   //二分找数\n    ll l=0,r=alls.size()-1;\n    while(l<r){\n        ll mid=l+r>>1;\n        if(alls[mid]>=x)r=mid;\n        else l=mid+1;\n    }return r+1;\n}\nint main(){\n    ll n,m;\n    cin>>n>>m;\n    for(ll i=1;i<=n;i++){\n        ll x,c;\n        cin>>x>>c;\n        add.push_back({x,c});  //存储操作的值的坐标及值\n        alls.push_back(x);     //先将操作的数的坐标放进去\n    }\n    for(ll i=1;i<=m;i++){\n        ll l,r;\n        cin>>l>>r;\n        query.push_back({l,r}); //存储询问的坐标\n        alls.push_back(l);   //将操作的坐标放进去\n        alls.push_back(r);\n    }\n    //去重复的数并排序\n    sort(alls.begin(),alls.end());\n    alls.erase(unique(alls.begin(),alls.end()),alls.end());\n    //真正的把数加上去\n    for(auto item:add){\n        ll x=find(item.first);\n        a[x]+=item.second;//根据坐标把值加上去\n    }\n    for(ll i=1;i<=alls.size();i++)s[i]=s[i-1]+a[i];//前缀和\n    //这个区间的数的和\n    for(auto item:query){\n        ll l=find(item.first);\n        ll r=find(item.second);\n        cout<<s[r]-s[l-1]<<endl;\n    }\n    return 0;\n}\n\n```\n', '算法');
INSERT INTO `blog` VALUES (24, '快速幂（算法）', 1, '2024-11-23 15:21:40', 3, 0, 0, 0, '***代码***\n```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nll n;\nll bzd(ll a,ll b,ll p){\n    ll res=1%p;//可能是为了防止res过大导致溢出\n    while(b){\n        if(b&1)res=res*a%p;//因为最终的结果要%p\n        a=a*a%p;//相当于将a的2^n%p\n        b>>=1;//将b的二进制为往左移一位\n    }return res;\n}\nint main(){\n    cin>>n;\n    while(n--){\n        ll a,b,q;\n        scanf(\"%lld%lld%lld\",&a,&b,&q);\n        printf(\"%lld\\n\",bzd(a,b,q));\n    }\n}\n\n```\n', '算法');
INSERT INTO `blog` VALUES (25, '最大公约数（算法）', 1, '2024-11-23 15:21:30', 15, 1, 0, 0, '```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nll a,b;\n// 两个整数 a 和 b 的最大公约数等于 b 和 a 除以 b 的余数的最大公约数\nint gcd(ll a,ll b){\n    return b?gcd(b,a%b):a;\n}\nint main(){\n    ll n;\n    cin>>n;\n    while(n--){\n      cin>>a>>b;\n      cout<<gcd(a,b)<<endl;\n    }\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (26, '筛质数（算法）', 1, '2025-01-30 21:29:03', 16, 0, 0, 0, '```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=1e6+10;\nll primes[N],cnt;\nbool st[N];\nll n;\nll get_primes(){\n    for(ll i=2;i<=n;i++){           //注意筛不能自定义起点\n        if(!st[i])primes[cnt++]=i;          //将质数存储\n    for(ll j=0;primes[j]*i<=n;j++){\n        st[primes[j]*i]=1;      //将i的倍数进行标记\n        if(i%primes[j]==0)break;        //为了使所有数只遍历一次\n        }\n    }\n}\nint main(){\n    cin>>n;\n    get_primes();\n    cout<<cnt;\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (27, '约数个数（算法）', 1, '2025-04-06 13:00:46', 95, 1, 1, 0, '```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nunordered_map<ll,int> prime;\nll mod=1e9+7;\nint main(){\n    ll n;\n    cin>>n;\n    ll res=1;\n    while(n--){\n        ll x;\n        cin>>x;\n        for(ll i=2;i*i<=x;i++){\n            while(x%i==0){\n                x=x/i;\n                prime[i]++;\n            }\n        }if(x>1)prime[x]++;\n    }\n    for(auto t:prime)res=res*(t.second+1)%mod;\n    cout<<res<<endl;\n\n    return 0;\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (28, '约数之和（算法）', 1, '2025-04-10 14:58:44', 231, 3, 3, 0, '```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nunordered_map<ll,ll> q;\nll mod=1e9+7;\nint main(){\n    ll n;\n    cin>>n;\n    while(n--){\n        ll x;\n        cin>>x;\n        for(ll i=2;i*i<=x;i++){\n            // if(x%i==0)       加不加都行\n            while(x%i==0){\n                x/=i;\n                q[i]++;\n            }\n        }\n        if(x>1)q[x]++;\n    }\n    ll sum=1;\n    // ll res=1;            不能定义在这里，因为每次遍历q的时候不能让res保留之前的值\n    for(auto t:q){\n        ll a=t.first,b=t.second;\n        ll res=1;\n        while(b--)res=(res*a+1)%mod;//相当于q^b+q^b-1+………………+q^2+q^1+q^0\n        sum=sum*res%mod;\n    }cout<<sum;\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (29, '最长上升子序列（优化版）', 15, '2025-04-06 11:59:36', 51, 1, 1, 0, '**题目：**\n给定一个长度为 N 的数列，求数值严格单调递增的子序列的长度最长是多少。\n\n**输入格式**\n第一行包含整数 N。\n第二行包含 N 个整数，表示完整序列。\n\n**输出格式**\n输出一个整数，表示最大长度。\n\n**数据范围**\n1≤N≤100000，−10^9^≤数列中的数≤10^9^\n\n**输入样例：**\n`7\n3 1 2 1 8 5 6`\n**输出样例：**\n`4`\n**代码**\n```language\n//f数组用来存储选的数\n//二分是查找每一个数的最小的大于的它的一个数\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=1e5+10;\nll w[N],f[N];\nll n;\nint main()\n{\n    cin>>n;\n    for(ll i=1;i<=n;i++)cin>>w[i];\n\n    ll len=0;\n    for(ll i=1;i<=n;i++)\n    {\n        ll l=0,r=len;\n        while(l<r)\n        {\n            ll mid=(l+r+1)>>1;\n            if(f[mid]<w[i])l=mid;\n            else r=mid-1;\n        }\n        f[r+1]=w[i];//更新或者加入(当找的数不是最后一个这是更新，否则是加入)\n        if(r+1>len)len++;//找的数是最后一个数(f中的)时才执行\n        // for(ll i=1;i<=len;i++)cout<<f[i]<<\" \";\n        // cout<<endl;\n    }\n    cout<<len;\n    return 0;\n}\n```\n**举例：**\n```language\n输入：\n7\n3 1 2 1 8 5 6\n输出：\n3 \n1 \n1 2 \n1 2 \n1 2 8 \n1 2 5 \n1 2 5 6 \n```\n\n', '算法');
INSERT INTO `blog` VALUES (30, '拦截导弹（算法）', 1, '2025-04-06 12:32:54', 12, 1, 1, 0, '**题目：**\n某国为了防御敌国的导弹袭击，发展出一种导弹拦截系统。\n\n但是这种导弹拦截系统有一个缺陷：虽然它的第一发炮弹能够到达任意的高度，但是以后每一发炮弹都不能高于前一发的高度。\n\n某天，雷达捕捉到敌国的导弹来袭。\n\n由于该系统还在试用阶段，所以只有一套系统，因此有可能不能拦截所有的导弹。\n\n输入导弹依次飞来的高度（雷达给出的高度数据是不大于`30000`的正整数，导弹数不超过`1000`），计算这套系统最多能拦截多少导弹，如果要拦截所有导弹最少要配备多少套这种导弹拦截系统。\n\n**输入格式**\n共一行，输入导弹依次飞来的高度。\n\n**输出格式**\n第一行包含一个整数，表示最多能拦截的导弹数。\n\n第二行包含一个整数，表示要拦截所有导弹最少要配备的系统数。\n\n**数据范围**\n雷达给出的高度数据是不大于 `30000` 的正整数，导弹数不超过 `1000`。\n\n**输入样例：**\n`389 207 155 300 299 170 158 65`\n**输出样例：**\n`6`\n`2`\n\n**代码：**\n```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=1010;\nll h[N],q[N],n=1,cnt=1;    //q数组必定为单调上升的数组\nll dp[N],res=0;\nint main()\n{\n    while(cin>>h[n])\n    {\n        n++;\n    }\n\n    q[1]=h[1];    //将第一枚导致直接放到第一套导弹系统中\n\n    for(ll i=1;i<n;i++)\n    {\n        dp[i]=1;\n        for(ll j=1;j<i;j++)\n        {\n            if(h[j]>=h[i])dp[i]=max(dp[i],dp[j]+1);\n        }\n        res=max(res,dp[i]);\n\n        ll k=1;\n        while(k<=cnt&&q[k]<h[i])k++;   //遍历每一个导弹系统的最低位置，找到可以放该导弹的位置（最低高度大于等于当前高度）\n        if(k>cnt)  //找不到可以放的系统，就重新开一个系统\n        {\n            cnt++;\n            q[cnt]=h[i];\n        }\n        else q[k]=h[i];  //找到了可以放的系统\n        // for(ll i=1;i<=cnt;i++)cout<<q[i]<<\" \";\n        // cout<<endl;\n    }\n    cout<<res<<endl<<cnt;\n\n}\n```\n', '算法');
INSERT INTO `blog` VALUES (35, '修改Blog代码（项目）', 1, '2025-04-06 11:55:54', 16, 0, 1, 0, '**后端（没有解释，因为我懒）：**\n\n!!! abstract 数据链路层\n    @Update(\"UPDATE blog SET title = #{title},authorId=#{authorId}, content = #{content}, updateDate = #{updateDate} WHERE id = #{id}\")\n    int updateBlog(Blog blog);\n!!!\n\n!!! caution 服务层\n    @Override public int updateBlog(Blog blog) {\n        return blogMapper.updateBlog(blog);\n    }\n!!!\n!!! danger 控制层\n    @PostMapping(\"UpdateBlog\")\n    public RestBean<Blog> updateBlog(@RequestParam(\"title\") String title,\n                                     @RequestParam(\"content\") String content,\n                                     @RequestParam(\"blogId\") int blogId,\n                                     HttpServletRequest request){\n        int authorId=(Integer)request.getAttribute(\"id\");\n        Blog blog=new Blog(blogId,title,authorId,new Date(),content);\n        int status = blogService.updateBlog(blog);\n        if(status>0) return RestBean.success(\"修改成功！\");\n        else return RestBean.failure(503,\"出现错误，请联系管理员~\");\n    }\n!!!\n<hr>\n\n\n**前端：**\n!!! success 只有作者可以修改或者删除\n    <div v-if=\"blog.authorId===userStore.user.id\" class=\"absolute flex flex-wrap top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity duration-1000\">\n            <button @click.stop=\"CheckIsUpdate(blog)\" class=\"bg-blue-500 text-white px-2 py-1 rounded-md mr-2 font-kai\">修改</button>\n            <button @click.stop=\"DeleteBlog(blog.id)\" class=\"bg-red-500 text-white px-2 py-1  rounded-md font-kai\">删除</button>\n    </div>\n!!!\n\n!!! abstract 设置为固定位置只有在修改按钮被点击才会出现,同时会将被点击的blog的信息传入，以便直接显示在编辑框中\n    const Update=reactive({\n      title:\"\",\n      text:\"\",\n      blogId:0,\n    })\n    const IsUpdate=ref(false);\n    \n    const CheckIsUpdate=(blog)=>{\n      IsUpdate.value=!IsUpdate.value;\n      if(IsUpdate.value)\n      {\n        Update.title=blog.title;\n        Update.text=blog.content;\n        Update.blogId=blog.id;\n      }\n    }\n    ------------------------------------------------------\n     <div v-if=\"IsUpdate\" class=\" h-4/5 w-2/3 bg-gray-400 absolute mt-24 ml-64 p-4 rounded-xl bg-opacity-50\">\n      <div class=\"flex\">\n        <input v-model=\"Update.title\" class=\"animatedInput w-full\" placeholder=\"Your Title Here\">\n        <button class=\"rounded-full p-1  flex flex-nowrap\" >\n          <svg  xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\"size-9 text-black hover:text-red-500 hover:scale-110  active:scale-90\" @click=\"UpdateBlog\">\n            <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"M9 12.75 11.25 15 15 9.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z\" />\n          </svg>\n          <svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\"size-9 text-black hover:text-red-500 hover:scale-110  active:scale-90\" @click=\"CheckIsUpdate\">\n            <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"m9.75 9.75 4.5 4.5m0-4.5-4.5 4.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z\" />\n          </svg>\n        </button>\n      </div>\n      <MdEditor  class=\"h-4/5 mt-2 \" v-model=\"Update.text\"/>\n    </div>\n!!!\n\n\n\n', '项目');
INSERT INTO `blog` VALUES (36, '粒子特效（解释版----抄的）', 1, '2024-11-24 14:23:08', 9, 0, 1, 0, '***借鉴：***[粒子特效](https://blog.csdn.net/DACBE/article/details/105995732)\n\n**1、初始化基础属性** \n\n!!! abstract 在这里初始化一些基础属性，粒子大小啊、颜色啊、数量啊叭啦叭啦的，先过过眼，下面都会用到。\n    const canvas = document.getElementById(\'canvas\')\n    const ctx = canvas.getContext(\'2d\')\n    let width = window.innerWidth\n    let height = window.innerHeight\n     \n    let dotsNum = 80 // 点的数量\n    let radius = 1 // 圆的半径，连接线宽度的一半\n    let fillStyle = \'rgba(255,255,255,0.5)\' // 点的颜色\n    let lineWidth = radius * 2\n    let connection = 120 // 连线最大距离\n    let followLength = 80 // 鼠标跟随距离\n     \n    let dots = [] // 粒子集合\n    let animationFrame = null\n    let mouseX = null\n    let mouseY = null\n!!!\n\n**2.添加鼠标移动事件并实时更新鼠标坐标** \n!!! abstract 在这里实时更新全局的鼠标坐标值，为下面与鼠标连线的处理做准备。\n    function mouseMove (e) {\n      mouseX = e.clientX\n      mouseY = e.clientY\n    }\n     \n    function mouseOut (e) {\n      mouseX = null\n      mouseY = null\n    }\n     \n    document.onmousemove = mouseMove\n    document.onmouseout = mouseOut\n!!!\n\n**3、通过随机数生成粒子的坐标和横纵轴速度**\n!!! abstract 这一步比较简单，在构造函数里通过随机数对粒子的横纵坐标和速度进行初始化，这里注意，速度是有正负值的，我在这把速度限制在-1到1之间，另外有个follow属性非常重要，在接下来我会讲到。\n    class Dot {\n        constructor(x, y) {\n          this.x = x\n          this.y = y\n          this.speedX = Math.random() * 2 - 1\n          this.speedY = Math.random() * 2 - 1\n          this.follow = false\n        }\n    }\n!!!\n**4、渲染粒子并将粒子对象保存在数组中**\n!!! abstract 这一步循环生成粒子对象并调用粒子对象的draw方法进行渲染，然后把粒子存入dots中以备后面使用。到这步完成，页面上就已经可以出现好多粒子了。\n    function initDots (num) { // 初始化粒子\n      ctx.fillStyle = fillStyle\n      ctx.lineWidth = lineWidth\n      for (let i = 0; i < num; i++) {\n        const x = Math.floor(Math.random() * width)\n        const y = Math.floor(Math.random() * height)\n        const dot = new Dot(x, y)\n        dot.draw()\n        dots.push(dot)\n       }\n    }\n     \n     \n    draw () { // class Dot\n      ctx.beginPath()\n      ctx.arc(this.x, this.y, radius, 0, 2 * Math.PI)\n      ctx.fill()\n      ctx.closePath()\n    }\n!!!\n**5、调用requestAnimationFrame启动动画，使粒子移动起来**\n!!! abstract 清空画布并调用粒子的move方法重新计算位置，这里使用window.requestAnimationFrame来请求动画帧，这样实现的动画要比setIntervel效果要更好，更自然，不了解的小伙伴可以自行百度一下。\n    function moveDots () { // 移动并建立点与点之间的连接线\n      ctx.clearRect(0, 0, width, height)\n      for (const dot of dots) {\n        dot.move()\n      }\n      animationFrame = window.requestAnimationFrame(moveDots)\n    }\n!!!\n**6、通过横纵坐标和速度计算粒子位置**\n!!! abstract 这一步，主要对粒子进行碰撞检测，当检测到粒子贴近窗口边缘时，需要把碰撞所对应的速度分量改为其相反值，然后重新得出粒子的横纵坐标；在这个方法下还有四行处理，目的是对速度绝对值大于1的分量进行减速，这个是为后面鼠标点击散开而做的处理，后面我会说到。在这些处理都结束之后，会调用一个位置修正和与鼠标连线的处理，这两个我后面会说，都完成后，掉用draw重新绘制粒子。\n    move () { // class Dot\n      if (this.x >= width || this.x <= 0) this.speedX = -this.speedX\n      if (this.y >= height || this.y <= 0) this.speedY = -this.speedY\n      this.x += this.speedX\n      this.y += this.speedY\n      if (this.speedX >= 1) this.speedX--\n      if (this.speedX <= -1) this.speedX++\n      if (this.speedY >= 1) this.speedY--\n      if (this.speedY <= -1) this.speedY++\n      this.correct()\n      this.connectMouse()\n      this.draw()\n    }\n\n!!!\n\n**7、计算与鼠标距离进行坐标的修正**\n!!! abstract [点我（第7点）](https://blog.csdn.net/DACBE/article/details/105995732)\n    correct () { // 根据鼠标的位置修正 class dot\n      if (!mouseX || !mouseY) return\n      let lengthX = mouseX - this.x\n      let lengthY = mouseY - this.y\n      const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n      if (distance <= followLength) this.follow = true\n      else if (this.follow === true && distance > followLength && distance <= followLength + 8) {\n        let proportion = followLength / distance\n        lengthX *= proportion\n        lengthY *= proportion\n        this.x = mouseX - lengthX\n        this.y = mouseY - lengthY\n      } else this.follow = false\n    }\n\n!!!\n\n**8、计算与鼠标距离并进行连线**\n!!! abstract 这一步就是通过粒子的坐标和鼠标的坐标计算出相对距离，判断其如果小于连线距离，那么就与鼠标直接绘制连线，值得一提的是，这条连接线的透明度是随着粒子与鼠标的距离改变而改变的，距离越大，越趋近于透明，透明度最大为0.5，具体计算很简单，我就不说了，大家直接看代码吧。\n    if (mouseX && mouseY) {\n      let lengthX = mouseX - this.x\n      let lengthY = mouseY - this.y\n      const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n      if (distance <= connection) {\n        opacity = (1 - distance / connection) * 0.5\n        ctx.strokeStyle = `rgba(255,255,255,${opacity})`\n        ctx.beginPath()\n        ctx.moveTo(this.x, this.y)\n        ctx.lineTo(mouseX, mouseY);\n        ctx.stroke();\n        ctx.closePath()\n      }\n    }\n\n!!!\n\n**9、计算粒子直接的距离并进行连线**\n!!! abstract 这一步和上一步同理，只不过是变成了判断两个粒子之间的距离进行连线，在这里用了一个双重for循环，值得注意的是里面的哪层for循环的起始值不是0，要不每条连接线会绘制两次，就不对了。和上一步一样，粒子之间的连接线也是随着距离变化透明度变化的。\n    for (let i = 0; i < dots.length; i++) {\n      for (let j = i; j < dots.length; j++) {\n        const distance = Math.sqrt((dots[i].x - dots[j].x) ** 2 + (dots[i].y - dots[j].y) ** 2)\n        if (distance <= connection) {\n          opacity = (1 - distance / connection) * 0.5\n          ctx.strokeStyle = `rgba(255,255,255,${opacity})`\n          ctx.beginPath()\n          ctx.moveTo(dots[i].x, dots[i].y)\n          ctx.lineTo(dots[j].x, dots[j].y);\n          ctx.stroke();\n          ctx.closePath()\n        }\n      }\n    }\n!!!\n\n**10、添加鼠标点击事件并调用粒子的散开事件**\n!!! abstract 在这一步添加一个全局的点击事件，这个事件会调用所有粒子的elastic方法，并判断是否执行散开动作。\n    function mouseClick () {\n      for (const dot of dots) dot.elastic()\n    }\n     \n    document.onclick = mouseClick\n!!!\n\n**11、通过与鼠标的距离和相对位置进行计算来重新给粒子添加速度**\n!!! abstract [点我（第11点）](https://blog.csdn.net/DACBE/article/details/105995732)\n    elastic () { // 鼠标点击后的弹射\n      let lengthX = mouseX - this.x\n      let lengthY = mouseY - this.y\n      const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n      if (distance >= connection) return\n      const rate = 1 - distance / connection // 距离越小此值约接近1\n      this.speedX = 40 * rate * -lengthX / distance\n      this.speedY = 40 * rate * -lengthY / distance\n    }\n     \n    // 上面粒子减速的处理\n    if (this.speedX >= 1) this.speedX--\n    if (this.speedX <= -1) this.speedX++\n    if (this.speedY >= 1) this.speedY--\n    if (this.speedY <= -1) this.speedY++\n!!!\n\n\n**12、监听页面大小变化来初始化画布**\n!!! abstract 最后，这是一个优化，因为用户改变浏览器窗口大小的时候，使画布可以根据改变后的大小重新加载，具体操作大家直接看代码吧，写博客的时候才想起来这个地方应该加个防抖的，懒得改了，有心的小伙伴们自己加一下吧哈哈。\n\n    function addCanvasSize () { // 改变画布尺寸\n      width = window.innerWidth\n      height = window.innerHeight\n      canvas.width = width\n      canvas.height = height\n      ctx.clearRect(0, 0, width, height)\n      dots = []\n      if (animationFrame) window.cancelAnimationFrame(animationFrame)\n      initDots(dotsNum)\n      moveDots()\n    }\n     \n    window.onresize = addCanvasSize\n\n!!!', '小知识');
INSERT INTO `blog` VALUES (37, '粒子特效（代码）', 1, '2025-04-08 15:53:01', 8, 0, 0, 0, '!!! success 代码--直接粘贴就好了\n    <template>\n      <div style=\"position: fixed; top: 0; left: 0; bottom: 0; right: 0; z-index: 0\">\n        <canvas ref=\"canvas\" style=\"background-color: rgb(50, 64, 87);\"></canvas>\n      </div>\n    </template>\n    \n    <script setup>\n    import { ref, onMounted, onBeforeUnmount } from \'vue\'\n    \n    const canvas = ref(null)\n    const ctx = ref(null)\n    const width = ref(window.innerWidth)\n    const height = ref(window.innerHeight)\n    \n    const dotsNum = 80 // 点的数量\n    const radius = 1 // 圆的半径，连接线宽度的一半\n    const fillStyle = \'rgba(255,255,255,0.5)\' // 点的颜色\n    const lineWidth = radius * 2\n    const connection = 120 // 连线最大距离\n    const followLength = 80 // 鼠标跟随距离\n    \n    const dots = ref([])\n    let animationFrame = null\n    let mouseX = null\n    let mouseY = null\n    \n    function addCanvasSize() { // 改变画布尺寸\n      width.value = window.innerWidth\n      height.value = window.innerHeight\n      canvas.value.width = width.value\n      canvas.value.height = height.value\n      ctx.value.clearRect(0, 0, width.value, height.value)\n      dots.value = []\n      if (animationFrame) window.cancelAnimationFrame(animationFrame)\n      initDots(dotsNum)\n      moveDots()\n    }\n    \n    function mouseMove(e) {\n      mouseX = e.clientX\n      mouseY = e.clientY\n    }\n    \n    function mouseOut(e) {\n      mouseX = null\n      mouseY = null\n    }\n    \n    function mouseClick() {\n      for (const dot of dots.value) dot.elastic()\n    }\n    \n    class Dot {\n      constructor(x, y) {\n        this.x = x\n        this.y = y\n        this.speedX = Math.random() * 2 - 1\n        this.speedY = Math.random() * 2 - 1\n        this.follow = false\n      }\n      draw() {\n        ctx.value.beginPath()\n        ctx.value.arc(this.x, this.y, radius, 0, 2 * Math.PI)\n        ctx.value.fill()\n        ctx.value.closePath()\n      }\n      move() {\n        if (this.x >= width.value || this.x <= 0) this.speedX = -this.speedX\n        if (this.y >= height.value || this.y <= 0) this.speedY = -this.speedY\n        this.x += this.speedX\n        this.y += this.speedY\n        if (this.speedX >= 1) this.speedX--\n        if (this.speedX <= -1) this.speedX++\n        if (this.speedY >= 1) this.speedY--\n        if (this.speedY <= -1) this.speedY++\n        this.correct()\n        this.connectMouse()\n        this.draw()\n      }\n      correct() { // 根据鼠标的位置修正\n        if (!mouseX || !mouseY) return\n        let lengthX = mouseX - this.x\n        let lengthY = mouseY - this.y\n        const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n        if (distance <= followLength) this.follow = true\n        else if (this.follow === true && distance > followLength && distance <= followLength + 8) {\n          let proportion = followLength / distance\n          lengthX *= proportion\n          lengthY *= proportion\n          this.x = mouseX - lengthX\n          this.y = mouseY - lengthY\n        } else this.follow = false\n      }\n      connectMouse() { // 点与鼠标连线\n        if (mouseX && mouseY) {\n          let lengthX = mouseX - this.x\n          let lengthY = mouseY - this.y\n          const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n          if (distance <= connection) {\n            let opacity = (1 - distance / connection) * 0.5\n            ctx.value.strokeStyle = `rgba(255,255,255,${opacity})`\n            ctx.value.beginPath()\n            ctx.value.moveTo(this.x, this.y)\n            ctx.value.lineTo(mouseX, mouseY);\n            ctx.value.stroke();\n            ctx.value.closePath()\n          }\n        }\n      }\n      elastic() { // 鼠标点击后的弹射\n        let lengthX = mouseX - this.x\n        let lengthY = mouseY - this.y\n        const distance = Math.sqrt(lengthX ** 2 + lengthY ** 2)\n        if (distance >= connection) return\n        const rate = 1 - distance / connection // 距离越小此值约接近1\n        this.speedX = 40 * rate * -lengthX / distance\n        this.speedY = 40 * rate * -lengthY / distance\n      }\n    }\n    \n    function initDots(num) { // 初始化粒子\n      ctx.value.fillStyle = fillStyle\n      ctx.value.lineWidth = lineWidth\n      for (let i = 0; i < num; i++) {\n        const x = Math.floor(Math.random() * width.value)\n        const y = Math.floor(Math.random() * height.value)\n        const dot = new Dot(x, y)\n        dot.draw()\n        dots.value.push(dot)\n      }\n    }\n    \n    function moveDots() { // 移动并建立点与点之间的连接线\n      ctx.value.clearRect(0, 0, width.value, height.value)\n      for (const dot of dots.value) {\n        dot.move()\n      }\n      for (let i = 0; i < dots.value.length; i++) {\n        for (let j = i; j < dots.value.length; j++) {\n          const distance = Math.sqrt((dots.value[i].x - dots.value[j].x) ** 2 + (dots.value[i].y - dots.value[j].y) ** 2)\n          if (distance <= connection) {\n            let opacity = (1 - distance / connection) * 0.5\n            ctx.value.strokeStyle = `rgba(255,255,255,${opacity})`\n            ctx.value.beginPath()\n            ctx.value.moveTo(dots.value[i].x, dots.value[i].y)\n            ctx.value.lineTo(dots.value[j].x, dots.value[j].y);\n            ctx.value.stroke();\n            ctx.value.closePath()\n          }\n        }\n      }\n      animationFrame = window.requestAnimationFrame(moveDots)\n    }\n    \n    onMounted(() => {\n      ctx.value = canvas.value.getContext(\'2d\')\n      addCanvasSize()\n      initDots(dotsNum)\n      moveDots()\n    \n      document.onmousemove = mouseMove\n      document.onmouseout = mouseOut\n      document.onclick = mouseClick\n      window.onresize = addCanvasSize\n    })\n    \n    onBeforeUnmount(() => {\n      if (animationFrame) window.cancelAnimationFrame(animationFrame)\n      document.onmousemove = null\n      document.onmouseout = null\n      document.onclick = null\n      window.onresize = null\n    })\n    </script>\n    \n    <style scoped>\n    </style>\n!!!', '小知识');
INSERT INTO `blog` VALUES (38, '最长公共子序列（算法）', 1, '2025-04-06 15:10:48', 43, 1, 1, 0, '**题目：**\n给定两个长度分别为 N 和 M 的字符串 A 和 B，求既是 A 的子序列又是 B 的子序列的字符串长度最长是多少。\n\n**输入格式**\n第一行包含两个整数 N 和 M。\n\n第二行包含一个长度为 N 的字符串，表示字符串 A。\n\n第三行包含一个长度为 M 的字符串，表示字符串 B。\n\n字符串均由小写字母构成。\n\n**输出格式**\n输出一个整数，表示最大长度。\n\n\n!!! example 数据范围\n1≤N,M≤1000\n!!!\n\n!!! example 输入样例\n4 5\nacbd\nabedc\n!!!\n!!! example 输出样例：\n3\n!!!\n![](https://cdn.acwing.com/media/article/image/2024/11/12/358712_17d3792ea0-%E6%9C%80%E9%95%BF%E5%85%AC%E5%85%B1%E5%AD%90%E5%BA%8F%E5%88%97.png)\n**代码：**\n```language\n#include<bits/stdc++.h>\nusing namespace std;\ntypedef long long ll;\nconst ll N=1010;\nchar s1[N],s2[N] ;\nll n,m,dp[N][N];\nint main()\n{\n    cin>>n>>m;\n    for(ll i=1;i<=n;i++)cin>>s1[i];\n    for(ll i=1;i<=m;i++)cin>>s2[i];\n    \n    for(ll i=1;i<=n;i++)\n    {\n        for(ll j=1;j<=m;j++)\n        {\n            dp[i][j]=max(dp[i-1][j],dp[i][j-1]);\n            if(s1[i]==s2[j])dp[i][j]=max(dp[i][j],dp[i-1][j-1]+1);\n        }\n    }\n    cout<<dp[n][m];\n    \n}\n```\n', '算法');
INSERT INTO `blog` VALUES (40, '上传图片（Cloudinary）', 1, '2025-04-08 16:58:51', 20, 0, 0, 0, '**网址：**<u>[Cloudinary](https://console.cloudinary.com/pm/c-2d4473b8227ec4df6d68d3b3f62280/developer-dashboard)</u>\n!!! abstract 配置\n    npm install cloudinary-core\n!!!\n<hr/>\n\n**在Cloudinary中创建预设**\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1731739446/smc0zcg0rqs2egq4gnyc.png)\n!!! abstract 上传图片（没有签名，即Mode为unsigned,该代码会返回一个https的图片的url）\n    const uploadImage = () => {\n      return new Promise((resolve, reject) => {\n        if (!file.value) {\n          resolve(\'\');\n          return;\n        }\n    \n        const img = new Image();\n        img.src = localImageUrl.value;\n        img.onload = () => {//设计图片的大小\n          const canvas = document.createElement(\'canvas\');\n          const ctx = canvas.getContext(\'2d\');\n          const maxSize = 100; // 裁剪后的最大尺寸\n          let width = img.width;\n          let height = img.height;\n    \n          if (width > height) {\n            if (width > maxSize) {\n              height *= maxSize / width;\n              width = maxSize;\n            }\n          } else {\n            if (height > maxSize) {\n              width *= maxSize / height;\n              height = maxSize;\n            }\n          }\n    \n          canvas.width = width;\n          canvas.height = height;\n          ctx.drawImage(img, 0, 0, width, height);\n          canvas.toBlob((blob) => {\n            const formData = new FormData();\n            formData.append(\'file\', blob, file.value.name);\n            formData.append(\'upload_preset\', \'fz-image\'); // 替换为你的上传预设名称\n    \n            fetch(\'https://api.cloudinary.com/v1_1/dctxdyjk0/image/upload\', {//这里要写云名\n              method: \'POST\',\n              body: formData,\n            })\n                .then((response) => response.json())\n                .then((data) => {\n                  resolve(data.secure_url);\n                })\n                .catch((error) => {\n                  reject(error);\n                });\n          }, \'image/jpeg\');\n        };\n      });\n    };\n!!!', '小知识');
INSERT INTO `blog` VALUES (44, '网页部署', 1, '2025-04-08 15:56:15', 15, 0, 0, 0, '## *一、准备*\n\n1.首先需要一个**服务器**（**不建议使用国内的**，因为要备案）\n\n2.域名购买（也可以不买）\n\n3.在**宝塔面板**中安装一下内容\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434064/ysblsfks5alfachnneng.png)\n\n\n\n\n## **二、前端**\n\n### **1.代码处理**\n\n在前端的`main.js`文件中写入以下语句，不改的话手机端登陆不了\n\n```js\naxios.defaults.baseURL = \'https://域名或者服务器IP地址/api\'\n// axios.defaults.baseURL = \'http://localhost:8080\'\n```\n\n在前端的终端中运行\n\n```\nnpm run build\n```\n\n编译完成\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434157/y98va66ij5rqd0f9h4jn.png)\n\n\n\n会多出来一个文件夹\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434200/vvfokczufu866nhdabzb.png)\n\n\n将前端代码上传到宝塔面板\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434242/gw4q5wcurfmoszscqvp6.png)\n\n\n\n\n### 2.创建站点+申请证书\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434289/kro3taihkendiwmfh8tg.png)\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434329/ia2a1ohfes8yyuzvtobe.png)\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434359/um1e5bujql8p0u5gmhnw.png)\n\n\n\n### **3.处理刷新问题**\n\n在**配置文件**中添加以下代码\n\n```\nlocation / {\n            try_files $uri $uri/ @router;\n    #需要指向下面的@router否则会出现vue的路由在nginx中刷新出现404\n            index  index.html index.htm;\n        }\n	#对应上面的@router，主要原因是路由的路径资源并不是一个真实的路径，所以无法找到具体的文件\n    #因此需要rewrite到index.html中，然后交给路由在处理请求资源\n        location @router {\n            rewrite ^.*$ /index.html last;\n        }\n```\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434403/trslxbr5gsbvjiaessyb.png)\n\n\n\n\n\n\n## **三、后端**\n\n### 1.数据库的上传\n\n先将数据库导出\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434433/xrzpody6dkqmdca0mhhm.png)\n\n\n\n### 2.导入报错处理\n\n可能需要**修改编码**(报错的话，电脑为mysql 8.0   服务器mysql 5.7    高版本向低版本导入会出错)，可以使用`vs code`\n\n```css\n将sql文件中的所有：utf8mb4_0900_ai_ci替换为utf8_general_ci，utf8mb4替换为utf8保存后再次运行sql文件\n```\n\n### 3.代码修改\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434470/hadspxbzzinaahl2qqai.png)\n\n\n\n```yaml\n//application.yml\nspring:\n  profiles:\n    active: dev\n```\n\n```yml\n//application-dev.yml\nspring:\n  datasource:\n    url: jdbc:mysql://localhost:3306/数据库名\n    #jdbc????? ????jdbc:mysql://????????:??/????\n    username: root\n    #?????\n    password: 19805601056Bkj@@\n    #?????\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    #??????\n\n#???MySQL????????????????????\n```\n\n```yml\n//application-pro.yml\nspring:\n  datasource:\n    url: jdbc:mysql://localhost:3306/数据库名（宝塔上面的）\n    #jdbc????? ????jdbc:mysql://????????:??/????\n    username: 数据库的用户名（宝塔上面的）\n    #?????\n    password: 数据库的密码（宝塔上面的）\n    #?????\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    #??????\n\n#???MySQL????????????????????\n```\n\n### 4.打包\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434499/fldrl4q6nnmjvb39nzv9.png)\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434532/yh75alfrdukczsdwsb0l.png)\n\n\n\n### 5.文件上传\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434568/numtvafsskxlkmi4wb0o.png)\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434596/nhvq1d3hbuzzxafk0vrj.png)\n\n\n\n### 6.统一前后端域名\n\n在以下界面中添加代码\n\n\n\n```css\nlocation /api {\n	proxy_pass http://localhost:8080;\n}\n```\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434778/zpwl0uzeitovh04eljx9.png)\n\n因为这里有一个`api`\n![](https://res.cloudinary.com/dctxdyjk0/image/upload/v1732434664/p72fntphoxl3mqizzlvo.png)\n\n', '项目');
INSERT INTO `blog` VALUES (45, '单词1', 1, '2025-04-06 13:28:00', 20, 0, 1, 0, '`flourish   兴旺;茁壮成长`\n`corridor  走廊;过道`\n`vocabulary  词汇（量）;词汇表`\n`specialize  专门研究（或从事）`\n`federal  联邦（制）的;联邦政府的`\n`identical  相同的`\n`label 标签;绰号`\n`interval 间隔;中场休息`\n`emerge 浮现;暴露; 变得显眼`\n`provison 供给（品）;给养;条款`\n`coverage 新闻报道;覆盖范围`\n`cast 投射;抛弃`\n`jail 监狱`\n`exploit 利用;剥削;发挥;开采`\n`stake 股份;赌注;桩`\n`manipulate 操纵;（熟练地）使用`\n`precaution 预防措施;防备`\n\n', '小知识');
INSERT INTO `blog` VALUES (46, '背包问题---总结', 1, '2025-04-10 14:57:25', 159, 0, 0, 0, '# 背包问题\n```mermaid\nflowchart TD \n  背包问题 --> 01背包\n  背包问题 --> 完全背包\n  背包问题 --> 多重背包\n  背包问题 --> 分组背包\n```\n##  01背包问题\n首先什么是01背包？\n**简述**：有一个背包体积为`V(最大的容量)`，现在有几个物品，每个物品的体积为`v[i]`价值为`w[i]`（**每个物品只有一个**），**不超过背包的容量情况下**，背包所背的**价值最大**\n<hr>\n\n### 分类讨论：\n**某一个状态下是否将某一个物品放到背包里面**\n 1.将**第i个物品放进去**，需要满足的条件是`当前物品的体积<背包的剩余的容量`\n ====> **从前`i-1`个物品里面选，背包的剩余容量为`j-v[i]`的最大价值**--------`对应的代码:dp[i][j]=max(dp[i-1][j],dp[i-1][j-v[i]]+w[i]);`\n 2.第i个物品不放进背包里面 ===> **从前`i-1`个物品里面选，背包的剩余体积为j的最大价值**--------`对应的代码:dp[i][j]=dp[i-1][j];`\n <hr>\n\n### 优化成一维--滚动数组（只需要最后的结果）\n首先观察二维的代码\n![](https://www.fzpersonalweb.xyz/api/uploads/b66d7d53-0225-4c6b-bbd9-972420ca8c47_01背包.png)\n我们可以发现每一个状态`i`都是由状态`i-1`更新得到的，那么我们是不是就是可以直接不管这个`i`---也就是将数组定义成一维的`dp[j]` \n\n关于为什么使用逆序而不是正序的解释：\n![](https://www.fzpersonalweb.xyz/api/uploads/960b90b4-f3a8-421a-948e-e78a0d5846eb_01背包优化逆序解释.png)\n\n```language\nfor(int i = 1; i <= n; i++)\n{\n    for(int j = m; j >= v[i]; j--)  \n        dp[j] = max(dp[j], dp[j - v[i]] + w[i]);\n} \n```\n <hr>\n\n##  完全背包\n**简述**：在**01背包**的基础上唯一发生变化的就是**每一个物品的数量不再是只有一个，而是无限个**\n<hr>\n\n### **朴素版：**\n对应的代码和01背包也差不多（就是加一个循环---**每一个物品取多少个**）：\n```language\nfor(ll k=0;k*v[i]<=j;k++)\n{\n   dp[i][j]=max(dp[i][j],dp[i-1][j-k*v[i]]+k*w[i]);\n    //k==0时就是不取i的情况\n}\n```\n<hr>\n\n### **优化版--对比法**\n将上述循环转化一下==>\n`dp[i][j]=max(dp[i-1][j],dp[i-1][j-v]+w,dp[i-1][j-2v]+2w......dp[i-1][j-k*v]+k*w);`\n其中的`k`表示的是当剩余体积为`j`时背包所能选当前物品的最大数量，即`j/v[i]`的最大值\n又因为`dp[i][j-v]=max(dp[i-1][j-v],dp[i-1][j-2v]+w,.....dp[i-1][j-v-(k-1)*v]+(k-1)*w);`\n二者进行相互对比：\n![](https://www.fzpersonalweb.xyz/api/uploads/b4367797-d6df-4b6f-ae90-1fbe14ae3a2e_完全背包.png)\n就可以将代码优化成：\n```language\ndp[i][j]=dp[i-1][j];\nif(j>=v[i])\n{\n   dp[i][j]=max(dp[i-1][j],dp[i][j-v[i]]+w[i]);\n}\n```\n<hr>\n\n### **优化版--滚动数组(目前还不会)**\n![敬请期待](https://tse1-mm.cn.bing.net/th/id/OIP-C.GVJNO3lexWUc1wAm7SsEBgHaHa?rs=1&pid=ImgDetMain)\n\n## 多重背包\n**简述**：在**完全背包**的基础上唯一发生变化的就是**每一个物品的数量不再是无限个，而是有限个（由题目提供）**\n<hr>\n\n### **朴素版**\n只需要在完全背包循环的基础上加一个**限制条件**---**没有超过该物品的数量**\n```language\nfor (int k = 0; k <= s[i] && k * v[i] <= j; k ++ )\n    dp[i][j] = max(dp[i][j], dp[i - 1][j - v[i] * k] + w[i] * k);\n```\n其中`s[i]表示物品的最大数量`\n<hr>\n\n### **优化版--二进制**\n就是将每一个物品的数量**采用二进制进行划分**（因为二进制可以表示所有的数）成多个组，**每一组的数量为1,2,4,8,...,2^k^,其中k是满足2^k^<=s的最大值**，多出来的数单独作为一组\n\n分完组后可以**通过组合实现原本该物品任意的数量----转换成了01背包的问题**\n对物品进行分组的代码：\n```language\n  for(ll i=1;i<=n;i++)//分组--转换成01背包问题\n    {\n        ll a,b,s;\n        cin>>a>>b>>s;\n        ll k=1;\n        while(k<=s)\n        {\n            cnt++;\n            v[cnt]=a*k;\n            w[cnt]=b*k;\n            s-=k;\n            k<<=1;\n        } \n        if(s>0)//多出来的数\n        {\n            cnt++;\n            v[cnt]=a*s;\n            w[cnt]=b*s;\n        }\n    }\n```\n', '算法');

-- ----------------------------
-- Table structure for favourite
-- ----------------------------
DROP TABLE IF EXISTS `favourite`;
CREATE TABLE `favourite`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NULL DEFAULT NULL,
  `blogId` int NULL DEFAULT NULL,
  `favouritetime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favourite
-- ----------------------------
INSERT INTO `favourite` VALUES (57, 1, 22, '2024-10-21 21:29:51');
INSERT INTO `favourite` VALUES (58, 1, 23, '2024-10-21 21:35:19');
INSERT INTO `favourite` VALUES (60, 14, 28, '2024-10-21 22:03:57');
INSERT INTO `favourite` VALUES (62, 1, 25, '2024-10-24 18:33:00');
INSERT INTO `favourite` VALUES (63, 1, 28, '2024-10-25 10:45:52');
INSERT INTO `favourite` VALUES (64, 1, 27, '2024-10-25 14:52:39');
INSERT INTO `favourite` VALUES (65, 15, 28, '2024-10-27 18:12:40');
INSERT INTO `favourite` VALUES (67, 1, 29, '2024-10-28 21:06:14');
INSERT INTO `favourite` VALUES (68, 1, 30, '2024-11-09 13:31:01');
INSERT INTO `favourite` VALUES (69, 1, 38, '2024-11-14 14:34:41');

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `blogId` int NOT NULL,
  `likeTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `userId`, `blogId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (107, 1, 22, '2024-10-21 21:29:50');
INSERT INTO `like` VALUES (108, 1, 23, '2024-10-21 21:35:20');
INSERT INTO `like` VALUES (109, 1, 28, '2024-10-21 21:52:25');
INSERT INTO `like` VALUES (110, 14, 28, '2024-10-21 22:03:55');
INSERT INTO `like` VALUES (112, 1, 27, '2024-10-25 14:52:05');
INSERT INTO `like` VALUES (113, 15, 28, '2024-10-27 18:12:39');
INSERT INTO `like` VALUES (115, 1, 29, '2024-10-28 21:06:18');
INSERT INTO `like` VALUES (116, 1, 30, '2024-11-09 13:31:05');
INSERT INTO `like` VALUES (117, 1, 35, '2024-11-10 11:20:38');
INSERT INTO `like` VALUES (118, 1, 38, '2024-11-14 14:34:40');
INSERT INTO `like` VALUES (119, 15, 36, '2024-11-14 20:03:57');
INSERT INTO `like` VALUES (120, 1, 45, '2024-12-17 19:09:58');

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0',
  `time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES (8, 'DeepSeek', 'https://chat.deepseek.com/', NULL);
INSERT INTO `link` VALUES (10, '图标', 'https://heroicons.com/', NULL);
INSERT INTO `link` VALUES (11, '正则表达式', 'https://www.runoob.com/?s=%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F', NULL);
INSERT INTO `link` VALUES (13, 'Vue', 'https://www.w3schools.com/vue/index.php', NULL);
INSERT INTO `link` VALUES (14, '123', 'http://www.fztestweb.top/', NULL);
INSERT INTO `link` VALUES (15, 'AcWing', 'https://www.acwing.com/activity/content/11/', NULL);
INSERT INTO `link` VALUES (16, '', '', NULL);
INSERT INTO `link` VALUES (17, '', '', NULL);

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `type` tinyint NULL DEFAULT NULL,
  `createTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (81, '点赞通知', '您的文章:\"单词1\"刚刚被用户uid:1点赞了。', 0, '2024-12-17 19:09:58');
INSERT INTO `notification` VALUES (82, '点赞通知', '您的文章:\"背包问题---总结\"刚刚被用户uid:1点赞了。', 0, '2025-04-06 15:51:47');
INSERT INTO `notification` VALUES (83, '点赞通知', '您的文章:\"背包问题---总结\"刚刚被用户uid:1点赞了。', 0, '2025-04-09 18:38:23');

-- ----------------------------
-- Table structure for plan
-- ----------------------------
DROP TABLE IF EXISTS `plan`;
CREATE TABLE `plan`  (
  `plan` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `finish` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `authorId` int NULL DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of plan
-- ----------------------------
INSERT INTO `plan` VALUES ('实现写日记的功能', 'false', '2024-12-02 19:05:48', 1, 2);
INSERT INTO `plan` VALUES ('刷题', 'false', '2024-12-01 12:23:27', 1, 3);
INSERT INTO `plan` VALUES ('算法训练', 'false', '2024-12-01 12:19:43', 1, 4);
INSERT INTO `plan` VALUES ('项目制作', 'false', '2024-12-01 12:19:45', 1, 5);
INSERT INTO `plan` VALUES ('网站完善', 'true', '2024-12-03 09:52:41', 1, 6);
INSERT INTO `plan` VALUES ('实验室网站启动', 'true', '2024-12-03 09:52:48', 1, 7);
INSERT INTO `plan` VALUES ('其它2', 'true', '2024-12-03 09:53:03', 1, 9);
INSERT INTO `plan` VALUES ('其它3', 'true', '2024-12-03 09:53:02', 1, 10);
INSERT INTO `plan` VALUES ('其它4', 'false', '2024-12-01 12:20:13', 1, 11);
INSERT INTO `plan` VALUES ('其它5', 'false', '2024-12-01 12:13:15', 1, 12);
INSERT INTO `plan` VALUES ('其它6', 'false', '2024-12-01 12:13:17', 1, 13);
INSERT INTO `plan` VALUES ('其它7', 'false', '2024-12-01 12:13:20', 1, 14);
INSERT INTO `plan` VALUES ('其它8', 'false', '2024-12-01 12:13:23', 1, 15);
INSERT INTO `plan` VALUES ('其它9', 'false', '2024-12-01 12:13:27', 1, 16);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `blogId` int NULL DEFAULT NULL,
  `question` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `authorId` int NULL DEFAULT NULL,
  `replyId` int NULL DEFAULT NULL,
  `authoravator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (97, 45, '好丑呀', '2024-12-02 13:14:54', 1, 0, 'https://narcissism-66.github.io/image/good.jpg', 'fz');
INSERT INTO `review` VALUES (98, 28, '123', '2025-04-06 13:01:27', 1, 0, 'https://narcissism-66.github.io/image/good.jpg', 'fz');

-- ----------------------------
-- Table structure for rs_notification
-- ----------------------------
DROP TABLE IF EXISTS `rs_notification`;
CREATE TABLE `rs_notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `notiId` int NOT NULL,
  `isRead` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rs_notification
-- ----------------------------
INSERT INTO `rs_notification` VALUES (73, 1, 81, 0);
INSERT INTO `rs_notification` VALUES (74, 1, 82, 0);
INSERT INTO `rs_notification` VALUES (75, 1, 83, 0);

-- ----------------------------
-- Table structure for rs_review
-- ----------------------------
DROP TABLE IF EXISTS `rs_review`;
CREATE TABLE `rs_review`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NULL DEFAULT NULL,
  `authorId` int NULL DEFAULT NULL,
  `reviewId` int NULL DEFAULT NULL,
  `reviewtime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `blogId` int NULL DEFAULT NULL,
  `replyId` int NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `useravator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rs_review
-- ----------------------------
INSERT INTO `rs_review` VALUES (86, 1, 1, 97, '2024-12-02 13:14:55', 45, 0, 'fz', 'https://narcissism-66.github.io/image/good.jpg', NULL);
INSERT INTO `rs_review` VALUES (87, 1, 1, 98, '2025-04-06 13:01:28', 28, 0, 'fz', 'https://narcissism-66.github.io/image/good.jpg', NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avator` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IsOnline` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `OnlineCount` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'fz', '19805601056', '123@qq.com', 'http://localhost:8080/uploads/7007c173-138a-4ad2-a300-6a6c2b16d0f1_可爱.png', '创始人', 'false', 48);
INSERT INTO `users` VALUES (14, '123', '123', '123@qq.om', 'https://narcissism-66.github.io/image/99.jpg', '用户', 'false', 48);
INSERT INTO `users` VALUES (15, '222', '222', '222', 'https://narcissism-66.github.io/image/1.jpg', '游客', 'false', 48);
INSERT INTO `users` VALUES (25, '111', '111', '123@qq.com', 'https://res.cloudinary.com/dctxdyjk0/image/upload/v1731758674/pxvsxwv9kip7ikz3bixd.jpg', '用户', 'false', 48);

SET FOREIGN_KEY_CHECKS = 1;
