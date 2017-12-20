
(()=> {
  let S = '\\';

  class DirectoryTools
  {
    /**
     *
     * @param path
     * @param fs
     * @param vm
     * @param context
     */
    constructor(path, context)
    {
      if (path.indexOf(S) === -1 && path.indexOf('/') !== -1)
        S = "/";
      const fs = require('fs');
      const vm = require('vm');
      this.path = path;
      this.js = path.substr(0, path.indexOf(`${S}src${S}test${S}`)) + `${S}src${S}main${S}webapp${S}static${S}js${S}`;
      this._include = function (path) {
        const code = fs.readFileSync(path);
        vm.runInThisContext(code, path);
      }.bind(context);

      Object.freeze(this);
    }

    /**
     * Includes JS scripts from the front end for testing.
     * @param path
     */
    include(path)
    {
      path = path.replace(/[\\|\/]/g,S);
      if (path.indexOf(S) === 0)
        path = path.substr(1);
      this._include(this.js + path);
    }
  }

  module.exports = DirectoryTools;
})();