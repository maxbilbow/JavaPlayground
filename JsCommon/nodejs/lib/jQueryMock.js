
(() => {
  "use strict";

  if (global['document'] === undefined)
    global.document = {
      createElement: (type) => `<${type}></${type}>`,
      name: 'document'
    };

  if (global['window'] === undefined)
    global.window = function window() {};

  const _idListeners = new Map();
  const _objectListeners = new WeakMap();
  function getListeners(element, event)
  {
    let _listeners = _idListeners;
    if (typeof element !== 'string')
      if (element.name)
        element = element.name;
      else
        _listeners = _objectListeners;

    let map = _listeners.get(element);
    if (!map)
      _listeners.set(element, map = {});

    let list = map[event];
    if (!list)
      map[event] = list = [];
    return list;
  }

  class MockAjax
  {
    constructor(outcome, ...response)
    {
      this.outcome = outcome;
      this.response = response;
    }

    success(fn)
    {
      this.outcome === 'success' && fn.apply(null,this.response);
      return this;
    }

    error(fn)
    {
      this.outcome === 'error' && fn.apply(null,this.response);
      return this;
    }

    fail(fn)
    {
      this.outcome === 'fail' && fn.apply(null,this.response);
      return this;
    }

    done(fn)
    {
      fn.apply(null,this.response);
      return this;
    }

    always(fn)
    {
      fn.apply(null,this.response);
      return this;
    }
  }

  class jQueryMock {
    constructor(element)
    {
      this.element = element;
    }

    ready (fn) {
      fn();
      return this;
    }

    get() {
      return $.mock.Ajax.success();
    }

    bind (event, fn)  {
      let listeners = getListeners(this.element, event);
      listeners.push(fn);
      return this;
    };

    on (event, fn)
    {
      return this.bind(event,fn);
    }


    unbind(event,fn){
      const listeners = getListeners(this.element,event);
      const index = listeners.indexOf(fn);
      if (index !== -1)
        listeners.splice(index,1);
      return this;
    };

    trigger(event, ...args)
    {
      const listeners = getListeners(this.element,event);
      listeners.forEach((fn) => fn.apply(this.element, args));
      return this;
    };
  }



  const $ = (element) => {
    return new jQueryMock(element);
  };

  $.mock = (methodName, fn) => {
    $[methodName] = fn;
    return $;
  };

  $.mock.get = (fn) => {
    $.mock('get', fn);
  };

  $.mock.Ajax = {
    success: (response) => new MockAjax('success', response),
    fail: (response) => new MockAjax('fail', response),
    error: (response) => new MockAjax('error', response)
  };

  $.mock.window = () => {
    return global.window;
  };

  $.mock.document = () => {
    return global.document;
  };

  module.exports = $;
})();