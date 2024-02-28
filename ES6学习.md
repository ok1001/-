**代码块内有效**

let 是在代码块内有效，var 是在全局范围内有效:

```js
{  
    let a = 0;  
    var b = 1; 
} 
a  // ReferenceError: a is not defined 
b  // 1
```

for 循环计数器很适合用 let

```js
for (var i = 0; i < 10; i++) {
  setTimeout(function(){
    console.log(i);
  })
}
// 输出十个 10
for (let j = 0; j < 10; j++) {
  setTimeout(function(){
    console.log(j);
  })
}
// 输出 0123456789
```

变量 i 是用 var 声明的，在全局范围内有效，所以全局中只有一个变量 i, 每次循环时，setTimeout 定时器里面的 i 指的是全局变量 i ，而循环里的十个 setTimeout 是在循环结束后才执行，所以此时的 i 都是 10。

变量 j 是用 let 声明的，当前的 j 只在本轮循环中有效，每次循环的 j 其实都是一个新的变量，所以 setTimeout 定时器里面的 j 其实是不同的变量，即最后输出 12345。（若每次循环的变量 j 都是重新声明的，如何知道前一个循环的值？这是因为 JavaScript 引擎内部会记住前一个循环的值）。



### 不存在变量提升

let 不存在变量提升，var 会变量提升:

```js
console.log(a);  //ReferenceError: a is not defined
let a = "apple";
 
console.log(b);  //undefined
var b = "banana";
```

变量 b 用 var 声明存在变量提升，所以当脚本开始运行的时候，b 已经存在了，但是还没有赋值，所以会输出 undefined。

变量 a 用 let 声明不存在变量提升，在声明变量 a 之前，a 不存在，所以会报错。



暂时性死区:

```js
var PI = "a";
if(true){
  console.log(PI);  // Cannot access 'PI' before initialization
  const PI = "3.1415926";
}
```

这段代码展示了在 JavaScript 中 `const` 关键字的一个关键特性：`const` 声明的变量具有块级作用域（block scope），并且它们会被提升（hoisted）到它们所在的作用域的顶部，但它们的初始化（赋值）不会被提升。

ES6 明确规定，代码块内如果存在 let 或者 const，代码块会对这些命令声明的变量从块的开始就形成一个封闭作用域。代码块内，在声明变量 PI 之前使用它会报错。



const 如何做到变量在声明初始化之后不允许改变的？其实 const 其实保证的不是变量的值不变，而是保证变量指向的内存地址所保存的数据不允许改动。此时，你可能已经想到，简单类型和复合类型保存值的方式是不同的。是的，对于简单类型（数值 number、字符串 string 、布尔值 boolean）,值就保存在变量指向的那个内存地址，因此 const 声明的简单类型变量等同于常量。而复杂类型（对象 object，数组 array，函数 function），变量指向的内存地址其实是保存了一个指向实际数据的指针，所以 const 只能保证指针是固定的，至于指针指向的数据结构变不变就无法控制了，所以使用 const 声明复杂类型对象时要慎重。



## ES6 解构赋值

解构赋值是对赋值运算符的扩展。

他是一种针对数组或者对象进行模式匹配，然后对其中的变量进行赋值。

在代码书写上简洁且易读，语义更加清晰明了；也方便了复杂对象中数据字段获取。



当解构模式有匹配结果，且匹配结果是 undefined 时，会触发默认值作为返回结果。

```js
let [a = 3, b = a] = [];     // a = 3, b = 3
let [a = 3, b = a] = [1];    // a = 1, b = 1
let [a = 3, b = a] = [1, 2]; // a = 1, b = 2
```

- a 与 b 匹配结果为 undefined ，触发默认值：**a = 3; b = a =3**
- a 正常解构赋值，匹配结果：a = 1，b 匹配结果 undefined ，触发默认值：**b = a =1**
- a 与 b 正常解构赋值，匹配结果：**a = 1，b = 2**



##  ES6 Symbol

ES6 引入了一种新的原始数据类型 Symbol ，表示独一无二的值，最大的用法是用来定义对象的唯一属性名。

ES6 数据类型除了 Number 、 String 、 Boolean 、 Object、 null 和 undefined ，还新增了 Symbol 。

Symbol 作为对象属性名时不能用.运算符，要用方括号。因为.运算符后面是字符串，所以取到的是字符串 sy 属性，而不是 Symbol 值 sy 属性。

```js
let syObject = {};
syObject[sy] = "kk";
 
syObject[sy];  // "kk"
syObject.sy;   // undefined
```



## Maps 和 Objects 的区别

- 一个 Object 的键只能是字符串或者 Symbols，但一个 Map 的键可以是任意值。
- Map 中的键值是有序的（FIFO 原则），而添加到对象中的键则不是。
- Map 的键值对个数可以从 size 属性获取，而 Object 的键值对个数只能手动计算。
- Object 都有自己的原型，原型链上的键名有可能和你自己在对象上的设置的键名产生冲突。

![img](D:\885767\Desktop\文档\其他文档\文档图片\1_HmXTMmVps1oJ7MU-odCpUA.jpeg)

1. **Object 都有自己的原型**：
   - 在JavaScript中，几乎所有的对象都是从其他对象继承而来的。每个对象都有一个内部链接，指向它的原型（prototype）。这个原型对象本身也可以有自己的原型，这样形成了一条原型链。
   - 例如，如果你创建一个普通的对象字面量 `{}`，这个对象的原型就是 `Object.prototype`。
2. **原型链上的键名有可能和你自己在对象上的设置的键名产生冲突**：
   - 当我们尝试访问一个对象的属性时，如果这个对象本身没有这个属性，JavaScript会沿着原型链继续查找。这意味着，如果原型链上的某个对象有一个属性名，而你的对象也有同名的属性，那么你访问这个属性时会得到你对象上的属性值，而不是原型链上的。
   - 这种情况就称为“键名冲突”。例如，如果你有一个对象 `obj`，它有一个属性 `name`，并且 `obj` 的原型也有一个 `name` 属性，那么当你访问 `obj.name` 时，你会得到 `obj` 自己的 `name` 属性值，而不是原型上的。

为了更好地理解，我们可以看一个简单的例子：

```javascript
function Person(name) {  
    this.name = name;  
}  
  
Person.prototype.name = "Default Name";  
  
const john = new Person("John");  
console.log(john.name);  // 输出 "John"，而不是 "Default Name"
```

在上述代码中，`Person` 的原型上有一个 `name` 属性，但我们为 `john` 对象设置了自己的 `name` 属性。当我们尝试访问 `john.name` 时，得到的是 `john` 对象自己的 `name` 属性值，而不是原型上的。

因此，这句话提醒我们在编写代码时要小心，确保我们不会意外地覆盖原型链上的属性或方法，这可能会导致预期之外的行为。



虽然 NaN 和任何值甚至和自己都不相等(NaN !== NaN 返回true)，NaN作为Map的键来说是没有区别的。

```js
var myMap = new Map();
myMap.set(NaN, "not a number");
 
myMap.get(NaN); // "not a number"
 
var otherNaN = Number("foo");
myMap.get(otherNaN); // "not a number"
```



```js
// Array 转 Set
var mySet = new Set(["value1", "value2", "value3"]);
// 用...操作符，将 Set 转 Array
var myArray = [...mySet];
String
// String 转 Set
var mySet = new Set('hello');  // Set(4) {"h", "e", "l", "o"}
// 注：Set 中 toString 方法是不能将 Set 转换成 String
```

**交集**

```js
var a = new Set([1, 2, 3]);
var b = new Set([4, 3, 2]);
var intersect = new Set([...a].filter(x => b.has(x))); // {2, 3}
```

**差集**

```js
var a = new Set([1, 2, 3]);
var b = new Set([4, 3, 2]);
var difference = new Set([...a].filter(x => !b.has(x))); // {1}
```

