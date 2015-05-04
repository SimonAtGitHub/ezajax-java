Ajax = function() {
    function request(url, opt) {
        function fn() {
        }
        var async = opt.async !== false, method = opt.method || 'GET', data = opt.data
            || null, success = opt.success || fn, failure = opt.failure
            || fn;
        method = method.toUpperCase();
        if (method == 'GET' && data) {
            url += (url.indexOf('?') == -1 ? '?' : '&') + data;
            data = null;
        }
        var xhr = window.XMLHttpRequest ? new XMLHttpRequest()
            : new ActiveXObject('Microsoft.XMLHTTP');
        xhr.onreadystatechange = function() {
            _onStateChange(xhr, success, failure);
        };
        xhr.open(method, url, async);
        if (method == 'POST') {
            xhr.setRequestHeader('Content-type',
                'application/x-www-form-urlencoded;');
        }
        xhr.send(data);
        return xhr;
    }
    function _onStateChange(xhr, success, failure) {
        if (xhr.readyState == 4) {
            var s = xhr.status;
            if (s >= 200 && s < 300) {
                success(xhr);
            } else {
                failure(xhr);
            }
        } else {
        }
    }
    return {
        request : request
    };
}();
if (typeof JSON !== 'object') {
    JSON = {};
}
(function() {
    'use strict';
    function f(n) {
        return n < 10 ? '0' + n : n;
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function() {

            return isFinite(this.valueOf()) ? this.getUTCFullYear() + '-'
                + f(this.getUTCMonth() + 1) + '-' + f(this.getUTCDate())
                + 'T' + f(this.getUTCHours()) + ':'
                + f(this.getUTCMinutes()) + ':' + f(this.getUTCSeconds())
                + 'Z' : null;
        };

        String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function() {
            return this.valueOf();
        };
    }

    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, gap, indent, meta = { // table
        // of
        // character
        // substitutions
        '\b' : '\\b',
        '\t' : '\\t',
        '\n' : '\\n',
        '\f' : '\\f',
        '\r' : '\\r',
        '"' : '\\"',
        '\\' : '\\\\'
    }, rep;

    function quote(string) {
        escapable.lastIndex = 0;
        return escapable.test(string) ? '"'
            + string.replace(escapable,
            function(a) {
                var c = meta[a];
                return typeof c === 'string' ? c : '\\u'
                    + ('0000' + a.charCodeAt(0).toString(16))
                    .slice(-4);
            }) + '"' : '"' + string + '"';
    }

    function str(key, holder) {
        var i, // The loop counter.
            k, // The member key.
            v, // The member value.
            length, mind = gap, partial, value = holder[key];
        if (value && typeof value === 'object'
            && typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }
        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }
        switch (typeof value) {
            case 'string':
                return quote(value);

            case 'number':
                return isFinite(value) ? String(value) : 'null';

            case 'boolean':
            case 'null':
                return String(value);
            case 'object':
                if (!value) {
                    return 'null';
                }
                gap += indent;
                partial = [];
                if (Object.prototype.toString.apply(value) === '[object Array]') {
                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }
                    v = partial.length === 0 ? '[]' : gap ? '[\n' + gap
                        + partial.join(',\n' + gap) + '\n' + mind + ']' : '['
                        + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }
                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        if (typeof rep[i] === 'string') {
                            k = rep[i];
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                } else {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                }
                v = partial.length === 0 ? '{}' : gap ? '{\n' + gap
                    + partial.join(',\n' + gap) + '\n' + mind + '}' : '{'
                    + partial.join(',') + '}';
                gap = mind;
                return v;
        }
    }
    if (typeof JSON.stringify !== 'function') {
        JSON.stringify = function(value, replacer, space) {
            var i;
            gap = '';
            indent = '';
            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }
            } else if (typeof space === 'string') {
                indent = space;
            }
            rep = replacer;
            if (replacer
                && typeof replacer !== 'function'
                && (typeof replacer !== 'object' || typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }
            return str('', {
                '' : value
            });
        };
    }
    if (typeof JSON.parse !== 'function') {
        JSON.parse = function(text, reviver) {
            var j;

            function walk(holder, key) {
                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }
            text = String(text);
            cx.lastIndex = 0;
            if (cx.test(text)) {
                text = text.replace(cx,
                    function(a) {
                        return '\\u'
                            + ('0000' + a.charCodeAt(0).toString(16))
                            .slice(-4);
                    });
            }
            if (/^[\],:{}\s]*$/
                .test(text
                    .replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
                    .replace(
                        /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
                        ']').replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
                j = eval('(' + text + ')');
                return typeof reviver === 'function' ? walk({
                    '' : j
                }, '') : j;
            }
            throw new SyntaxError('JSON.parse');
        };
    }
}());

EzayAjax = {
    invoke : function(params) {
        // 由于请求是采用post方式提交,不会存在缓存问题,所以后面的ran随机参数加不加都没有关系,但是为了以后万一转换到get方式做准备
        var url = "eazyajax/" + params.moduleName + "/" + params.methodName+".ac";
        var scriptElements = document.getElementsByTagName("script");
        for ( var i = scriptElements.length - 1; i >= 0; i--) {
            var element = scriptElements[i];
            if (element.src.lastIndexOf("eazyajax/core.js") != -1)
                url = element.src.replace("core.js", "") + params.moduleName
                + "/" + params.methodName + ".ac";
        }

        var returnValue = null;

        //先对参数进行编码
        var argsEncodedString = JSON.stringify(params.args)
            .replace(new RegExp("&","gm"),"{[and]}")
            .replace(new RegExp("\\+","gm"),"{[plus]}")
            .replace(new RegExp("-","gm"),"{[sub]}")
            .replace(new RegExp("#","gm"),"{[hash]}");
        argsEncodedString = encodeURI(argsEncodedString);

        if (params.callback == undefined) {
            //同步调用
            var xhr = Ajax.request(url, {
                async : false,
                method : "POST",
                data : "args=" + argsEncodedString
            });

            var returnJson = JSON.parse(xhr.responseText);
            if (returnJson.exception != null) {
                if (returnJson.exception.message != "")
                    throw new Error(returnJson.exception.message);
                else
                if(returnJson.exception.cause.message != "")
                    throw new Error(returnJson.exception.cause.message);
                else
                    throw new Error("未知错误");
            } else
                returnValue = returnJson.returnValue;
        } else {
            //异步调用
            if(typeof params.callback !== 'function')
                throw new Error("参数错误");

            Ajax.request(url, {
                async : true,
                method : "POST",
                data : "args=" + argsEncodedString,
                success : function(data) {
                    var returnJson = JSON.parse(data.responseText);
                    var error = null;
                    var returnValue = null;

                    if (returnJson.exception != null) {
                        if (returnJson.exception.message != "")
                            error = new Error(returnJson.exception.message);
                        else if (returnJson.exception.cause.message != "")
                            error = new Error(
                                returnJson.exception.cause.message);
                        else
                            error = new Error("未知错误");
                    }
                    else{
                        returnValue = returnJson.returnValue;
                    }

                    params.callback(returnValue, error);
                },
                failure : function() {
                    params.callback(null, new Error("AjaxError"));
                }
            });
        }
        return returnValue;
    },
    createParams : function(moduleName, methodName, callback, argsNum, args) {
        var argsArray = new Array(argsNum);
        for ( var i = 0; i < argsNum; i++)
            argsArray[i] = args[i];

        var params = {
            moduleName : moduleName,
            methodName : methodName,
            args : argsArray,
            callback : callback
        }

        return params;
    }
}
