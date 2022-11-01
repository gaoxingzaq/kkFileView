(window.webpackJsonp = window.webpackJsonp || [])
.push([
	[15, 27], {
		106: function(module, __webpack_exports__, __webpack_require__) {
			"use strict";
			__webpack_require__.r(__webpack_exports__), __webpack_require__.d(__webpack_exports__, "extManager", (function() {
				return extManager
			}));
			var psdetch_state__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(0),
				dexie__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(158),
				psdetch_utils_googleTrack__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(6);
			const key = [76, 89, 116, 116, 56, 108, 85, 102, 70, 111, 37, 106, 75, 81, 120, 35];
			class ExtManager {
				constructor() {
					this.db = new dexie__WEBPACK_IMPORTED_MODULE_1__.a("psdetch-ext"), this.db.version(2)
						.stores({
							extensions: "++id, name"
						})
				}
				async loadNativeExtension(e) {
					const t = new((await __webpack_require__.e(26)
							.then(__webpack_require__.t.bind(null, 201, 7)))
						.ModeOfOperation.ctr)(key);
					return new Promise((n, s) => {
						const o = new FileReader;
						o.onloadend = () => {
							const e = o.result,
								n = new Uint8Array(e),
								s = t.decrypt(n);
							this.loadJSExtension(new Blob([s]))
						}, o.readAsArrayBuffer(e)
					})
				}
				async loadJSExtension(file) {
					return new Promise((res, rej) => {
						const fr = new FileReader;
						fr.onloadend = () => {
							const script = fr.result;
							eval(script)
						}, fr.readAsText(file)
					})
				}
				get table() {
					return this.db.table("extensions")
				}
				async loadExtension(e) {
					const t = await this.table.where({
							name: e
						})
						.first();
					if (t) {
						const e = t.data;
						e && (e.name.match(/\.ext$/) ? this.loadNativeExtension(e) : this.loadJSExtension(e))
					}
				}
				async initExtension(e) {
					const t = e.default;
					t.init ? t.init(psdetch_state__WEBPACK_IMPORTED_MODULE_0__.b) : console.log("Not an extension")
				}
				async listExtensions() {
					const e = await this.table.orderBy("name")
						.primaryKeys(),
						t = await this.table.orderBy("name")
						.keys(),
						n = [];
					for (let s = 0; s < e.length; s++) n.push({
						id: e[s],
						name: t[s].toString()
					});
					return n
				}
				async loadAllExtensions() {
					const e = await this.table.orderBy("name")
						.keys();
					for (const t of e) await this.loadExtension(t.toString())
				}
				async clearTable() {
					return this.table.clear()
				}
				async addExtension(e, t) {
					Object(psdetch_utils_googleTrack__WEBPACK_IMPORTED_MODULE_2__.a)("ext_add_" + e), await this.registerExtension(e, t), await this.loadExtension(e)
				}
				async deleteExtension(e) {
					await this.table.delete(e)
				}
				async registerExtension(e, t) {
					await this.table.add({
						name: e,
						data: t
					})
				}
			}
			const extManager = new ExtManager;
			window.extManager = {
				initExtension: extManager.initExtension
			}
		},
		159: function(e, t, n) {
			"use strict";
			n.d(t, "a", (function() {
				return a
			}));
			var s = n(4),
				o = (n(344), n(6));
			class a extends s.a {
				constructor(e) {
					super(e), this.openFromDisk = () => {
						
						Object(o.a)("open_file_dlg");
					
					
								this.props.onFile(t)
						
					}, this.dragOver = e => {
						e.preventDefault()
					}, this.dropFile = e => {
						Object(o.a)("open_file_drop"), this.setState({
							dragging: ""
						}), e.preventDefault();
						
						const t = e.dataTransfer.files[0];
						//console.log(t);
						this.props.onFile(t)
					}, this.state = {
						curFile: null,
						dragging: ""
					}
				}
				render() {
					return Object(s.b)("div", {
						onDragEnter: () => {
							this.setState({
								dragging: "dragging"
							})
						},
						onDragLeave: () => {
							this.setState({
								dragging: ""
							})
						},
						onDragOver: this.dragOver,
						onDrop: this.dropFile,
						onClick: this.openFromDisk,
						class: `fileDropper ${this.state.dragging}`
					}, Object(s.b)("button", {
						class: "button is-primary"
					}, "点我打开文件"), this.state.curFile && Object(s.b)("div", {
						class: "loadedFile"
					}, "Loaded: ", this.state.curFile.name), Object(s.b))
				}
				
			}

		},
		160: function(e) {
			e.exports = {
				name: "psdetch",
				version: "3.2.0",
				description: "",
				scripts: {
					test: 'echo "Error: no test specified" && exit 1',
					dev: "sh -c \"echo 'Web Server on: http://127.0.0.1:8080' \" & webpack --watch & http-server -s -c-1 ./static  ",
					build: "rm -Rf ./static/build && NODE_ENV=production webpack",
					clean: "rm -Rf ./static/build ; rm -Rf ./static/assets;",
					obf: "script/obfuscate.js",
					"dev:ext": "webpack --watch --config ",
					"build:ext:all": "NODE_ENV=production script/buildextall.js",
					encrypt: "script/encrypt.js"
				},
				engines: {
					node: "~12.20.0"
				},
				private: !0,
				author: "",
				license: "ISC",
				dependencies: {
					"@types/aes-js": "^3.1.0",
					"aes-js": "^3.1.2",
					bulma: "^0.7.2",
					bulmaswatch: "^0.7.2",
					clipboard: "^2.0.4",
					dexie: "^2.0.4",
					"file-saver": "^2.0.0",
					fuckadblock: "^3.2.1",
					jszip: "^3.2.2",
					"mozjpeg-js": "^3.3.1",
					pako: "^1.0.11",
					paper: "^0.12.4",
					path: "^0.12.7",
					"pdfjs-dist": "^2.2.228",
					preact: "^8.3.0",
					"prop-types": "^15.7.2",
					psd: "^3.2.0",
					"react-adsense": "^0.1.0",
					redux: "^4.0.1",
					"sprintf-js": "^1.1.1",
					"svg.js": "^2.7.1",
					svgo: "^1.2.0",
					toastr: "^2.1.4",
					"upng-js": "^2.1.0",
					utif: "^3.1.0"
				},
				devDependencies: {
					"@fortawesome/fontawesome-free": "^5.7.2",
					"@types/clipboard": "^2.0.1",
					"@types/css-font-loading-module": "0.0.3",
					"@types/file-saver": "^2.0.0",
					"@types/jasmine": "^2.8.8",
					"@types/jszip": "^3.1.7",
					"@types/node": "^10.5.1",
					"@types/pdfjs-dist": "^2.1.3",
					"@types/sprintf-js": "^1.1.1",
					"@types/toastr": "^2.1.35",
					"@types/upng-js": "^2.1.1",
					"@types/webpack": "^4.4.25",
					autoprefixer: "^9.1.0",
					"awesome-typescript-loader": "^5.2.0",
					"coffee-loader": "^0.9.0",
					coffeescript: "^1.11.1",
					"copy-webpack-plugin": "^4.5.2",
					cpx: "^1.5.0",
					"css-loader": "^1.0.0",
					"dotenv-webpack": "^6.0.0",
					"file-loader": "^2.0.0",
					glob: "^7.1.6",
					"http-server": "^0.11.1",
					i: "^0.3.6",
					jasmine: "^3.1.0",
					"jasmine-core": "^3.1.0",
					"javascript-obfuscator": "^0.27.2",
					karma: "^2.0.4",
					"karma-chrome-launcher": "^2.2.0",
					"karma-commonjs": "^1.0.0",
					"karma-jasmine": "^1.1.2",
					"karma-jasmine-html-reporter": "^1.1.0",
					"karma-sourcemap-loader": "^0.3.7",
					"karma-typescript": "^3.0.12",
					"karma-webpack": "^3.0.0",
					"mini-css-extract-plugin": "^0.9.0",
					"node-sass": "^4.9.2",
					"obfuscator-loader": "^1.1.2",
					"postcss-loader": "^2.1.6",
					"raw-loader": "^1.0.0",
					"sass-loader": "^7.1.0",
					"script-loader": "^0.7.2",
					"source-map-loader": "^0.2.3",
					"style-loader": "^0.22.0",
					"terser-webpack-plugin": "^2.3.5",
					"ts-loader": "^4.4.2",
					tslint: "^5.11.0",
					"tslint-react": "^3.6.0",
					typescript: "^2.9.2",
					"uglify-es": "^3.3.9",
					"uglifyjs-webpack-plugin": "^2.2.0",
					"url-loader": "^4.0.0",
					webpack: "^4.28.4",
					"webpack-cli": "^3.1.0",
					"webpack-visualizer-plugin": "^0.1.11"
				},
				extensions: [{
					name: "sketchapp-adapter",
					version: "0.2.1",
					webpack: "./webpack-ext/sketchapp-adapter.js"
				}, {
					name: "xd-adapter",
					version: "0.2.1",
					webpack: "./webpack-ext/xd-adapter.js"
				}]
			}
		},
		161: function(e, t, n) {
			"use strict";
			n.d(t, "a", (function() {
				return a
			}));
			var s = n(4),
				o = (n(352), n(33));
			class a extends s.a {
				constructor() {
					super(), Object(o.a)(this, {
						adapters: e => e.sessionState.adapters
					})
				}
				prepareColumns() {
					const e = [],
						t = this.props.columnNum || 1,
						n = Math.ceil(this.state.adapters.length / t);
					let s = [];
					for (const t of this.state.adapters) s.push(t), s.length === n && (e.push(s), s = []);
					return s.length > 0 && e.push(s), e
				}
				render() {
					const e = this.prepareColumns();
					return Object(s.b)("div")
				}
			}
		},
		162: function(e, t, n) {
			"use strict";
			n.d(t, "a", (function() {
				return o
			}));
			var s = n(4);
			class o extends s.a {
				constructor() {
					super(), this.state = {
						loading: !1
					}
				}
				async buttonClick(e) {
					if (this.props.onClick) {
						const t = e.currentTarget,
							n = this.props.onClick(e);
						if (n instanceof Promise) {
							const e = t.clientWidth,
								s = t.clientHeight;
							t.style.width = e + "px", t.style.height = s + "px", this.setState({
								loading: !0
							}), await n, this.setState({
								loading: !1
							}), t.style.width = "", t.style.height = ""
						}
					}
				}
				render() {
					return Object(s.b)("button", {
						disabled: this.state.loading || this.props.disabled,
						onClick: e => this.buttonClick(e),
						class: this.props.class
					}, this.state.loading ? Object(s.b)("span", {
						class: "animated infinite spin"
					}, Object(s.b)("i", {
						class: "fas fa-spinner"
					})) : this.props.children)
				}
			}
		},
		31: function(e, t, n) {
			"use strict";
			n.r(t);
			var s = {};
			n.r(s), n.d(s, "rgbToHex", (function() {
				return i
			})), n.d(s, "rgbStrToHex", (function() {
				return c
			}));
			var o = n(2);

			function a(e) {
				var t = e.toString(16);
				return 1 == t.length ? "0" + t : t
			}

			function i(e, t, n) {
				return "#" + a(e) + a(t) + a(n)
			}
			const r = /\d+/g;

			function c(e) {
				if (!e) return i(0, 0, 0);
				const t = e.match(r);
				return t ? i(parseInt(t[0]), parseInt(t[1]), parseInt(t[2])) : ""
			}
			var l = n(3);

			function p(e) {
				return new Promise((t, n) => {
					setTimeout(t, e)
				})
			}
			var d = n(7),
				b = n(1);

			function u(e) {
				const t = new b.Progress,
					n = new XMLHttpRequest,
					s = e.url.trim(),
					o = d.basename(s);
				if (n.open("GET", s, !0), e.header)
					for (const t in e.header) e.hasOwnProperty(t) && n.setRequestHeader(t, e.header[t]);
				return n.responseType = "arraybuffer", n.addEventListener("load", () => {
					if (200 !== n.status) t.error(new Error(n.statusText));
					else {
						const e = n.getResponseHeader("content-type"),
							s = e ? e.split(";")[0] : "unknown",
							a = new Blob([n.response], {
								type: s
							});
						t.complete({
							meta: {
								name: o,
								mime: a.type
							},
							file: a
						})
					}
				}), n.addEventListener("progress", e => {
					e.lengthComputable && t.progress(e.loaded / e.total)
				}), n.send(), t
			}

			function f(e, t) {
				const n = h(t);
				return n ? e.clampedRelativeRect(n) : e
			}

			function g(e) {
				const t = h(e);
				if (t) {
					const n = document.createElement("canvas");
					return n.width = t.width, n.height = t.height, n.getContext("2d")
						.drawImage(e, t.left, t.top, t.width, t.height, 0, 0, t.width, t.height), n
				}
				return e
			}

			function h(e) {
				const t = e.getContext("2d"),
					n = e.width,
					s = e.height;
				if (0 === n || 0 === s) return;
				const o = t.getImageData(0, 0, n, s)
					.data;
				let a = 0,
					i = 0,
					r = s - 1,
					c = n - 1;
				for (a = 0; a < s; a++) {
					let e = !1;
					for (let t = 0; t < n; t++) {
						if (e = o[4 * (a * n + t) + 3] > 0, e) break
					}
					if (e) {
						a--;
						break
					}
				}
				for (i = 0; i < n; i++) {
					let e = !1;
					for (let t = 0; t < s; t++) {
						if (e = o[4 * (t * n + i) + 3] > 0, e) break
					}
					if (e) {
						i--;
						break
					}
				}
				for (r = s - 1; r >= 0; r--) {
					let e = !1;
					for (let t = 0; t < n; t++) {
						if (e = o[4 * (r * n + t) + 3] > 0, e) break
					}
					if (e) break
				}
				for (c = n - 1; c >= 0; c--) {
					let e = !1;
					for (let t = 0; t < s; t++) {
						if (e = o[4 * (t * n + c) + 3] > 0, e) break
					}
					if (e) break
				}
				return i > 0 || a > 0 || r < s - 1 || c < n - 1 ? new b.Rect(i, a, c + 1, r + 1) : void 0
			}

			function m(e) {
				const t = x();
				e = e.replace("<defs/>", ""), t.innerHTML = e;
				const n = t.firstChild;
				document.body.appendChild(t);
				const s = n.viewBox.baseVal || {
					x: 0,
					y: 0,
					width: 0,
					height: 0
				};
				let o = !1;
				const a = n.children;
				for (let e = 0; e < a.length; e++) {
					const t = a[e];
					if ("defs" === t.tagName) continue;
					const n = t.getBBox();
					0 !== n.width && 0 !== n.height && (o ? (s.x > n.x && (s.x = n.x), s.y > n.y && (s.y = n.y), s.width < n.width + n.x - s.x && (s.width = n.width + n.x - s.x), s.height < n.height + n.y - s.y && (s.height = n.height + n.y - s.y)) : (o = !0, s.x = n.x, s.y = n.y, s.width = n.width, s.height = n.height))
				}
				const i = t.innerHTML;
				return t.remove(), i
			}

			function x() {
				const e = document.createElement("div");
				return e.style.position = "absolute", e.style.left = "-999px", e.style.top = "-999px", document.querySelector("body")
					.appendChild(e), e
			}
			var A = n(108),
				v = n.n(A);

			function w(e) {
				const t = document.createElement("button"),
					n = new v.a(t, {
						text: function() {
							return e
						}
					});
				t.click(), n.destroy()
			}
			var j = n(55);
			n.d(t, "cachePromise", (function() {
				return l.a
			})), n.d(t, "funcWrapper", (function() {
				return l.b
			})), n.d(t, "sleep", (function() {
				return p
			})), n.d(t, "loadRemoteFile", (function() {
				return u
			})), n.d(t, "adjustPixelRect", (function() {
				return f
			})), n.d(t, "trimCanvas", (function() {
				return g
			})), n.d(t, "CanvasExportFormats", (function() {
				return o.CanvasExportFormats
			})), n.d(t, "canvasToImg", (function() {
				return o.canvasToImg
			})), n.d(t, "svgToCanvas", (function() {
				return o.svgToCanvas
			})), n.d(t, "canvasToFile", (function() {
				return o.canvasToFile
			})), n.d(t, "zoomImg", (function() {
				return o.zoomImg
			})), n.d(t, "cropCanvas", (function() {
				return o.cropCanvas
			})), n.d(t, "canvasToImgUrl", (function() {
				return o.canvasToImgUrl
			})), n.d(t, "svgToUrl", (function() {
				return o.svgToUrl
			})), n.d(t, "imgToCanvas", (function() {
				return o.imgToCanvas
			})), n.d(t, "scaleCanvas", (function() {
				return o.scaleCanvas
			})), n.d(t, "centerSvgStringViewBox", (function() {
				return m
			})), n.d(t, "invisibleDiv", (function() {
				return x
			})), n.d(t, "copyToClipboard", (function() {
				return w
			})), n.d(t, "canvas", (function() {
				return o
			})), n.d(t, "render", (function() {
				return j
			})), n.d(t, "color", (function() {
				return s
			}))
		},
		323: function(e, t, n) {
			"use strict";
			var s = n(4),
				o = (n(348), n(8));
			class a extends Error {
				constructor(e, t) {
					super(), this.patreonUrl = t, this.message = `${e} Download the extension at: ${t}`
				}
			}
			let i;
			async function r(e, t = "Error") {
				return e instanceof a ? async function(e, t = "Early Access") {
					return new Promise((n, s) => {
						i.setState({
							active: !0,
							mode: "patreon",
							title: t,
							content: e,
							onClose: () => {
								n()
							},
							backgroundDrop: !0
						})
					})
				}(e): async function(e, t = "Alert") {
					return new Promise((n, s) => {
						i.setState({
							active: !0,
							mode: "alert",
							title: t,
							content: e,
							onClose: () => {
								n()
							},
							backgroundDrop: !0
						})
					})
				}(e.toString(), t)
			}
			n.d(t, "b", (function() {
				return r
			})), n.d(t, "a", (function() {
				return c
			}));
			class c extends s.a {
				constructor() {
					super(), this.state = {
						active: !1,
						backgroundDrop: !0
					}, i = this
				}
				close(e) {
					this.state.onClose && this.state.onClose(e), this.setState({
						active: !1
					})
				}
				render() {
					return Object(s.b)("div", {
						class: `psdetch-modal modal ${this.state.active?"is-active":""}`
					}, Object(s.b)("div", {
						class: "modal-background",
						onClick: () => {
							this.state.backgroundDrop && this.close()
						}
					}), Object(s.b)("div", {
						class: "modal-card"
					}, Object(s.b)("header", {
						class: "modal-card-head"
					}, Object(s.b)("p", {
						class: "modal-card-title"
					}, this.state.title)), Object(s.b)("section", {
						class: "modal-card-body"
					}, this.state.content instanceof a ? this.state.content.message : this.state.content), Object(s.b)("footer", {
						class: "modal-card-foot"
					}, "alert" === this.state.mode && Object(s.b)("button", {
						onClick: () => this.close(),
						class: "button is-primary"
					}, Object(o.c)("ok")), "confirm" === this.state.mode && [Object(s.b)("button", {
						onClick: () => this.close(!1),
						class: "button is-danger"
					}, Object(o.c)("no")), Object(s.b)("button", {
						onClick: () => this.close(!0),
						class: "button is-success"
					}, Object(o.c)("yes"))], "patreon" === this.state.mode && [Object(s.b)("button", {
						onClick: () => this.close(!1),
						class: "button is-danger"
					}, Object(o.c)("no")), Object(s.b)("a", {
						target: "_blank",
						href: this.state.content.patreonUrl,
						class: "button is-primary"
					}, "Download")])))
				}
			}
		},
		33: function(e, t, n) {
			"use strict";
			n.d(t, "a", (function() {
				return o
			})), n.d(t, "b", (function() {
				return a
			}));
			var s = n(0);
			s.b.getState();

			function o(e, t) {
				const n = Object.keys(t);
				a(e, t);
				const o = s.b.getState(),
					i = {};
				e.state = {}, n.forEach(n => {
					const s = t[n](o);
					s instanceof Promise ? s.then(t => {
						e.state[n] !== t && e.setState({
							[n]: t
						})
					}, e => {
						console.log(e)
					}) : i[n] = s
				}), e.state = i
			}

			function a(e, t) {
				const n = Object.keys(t),
					o = e.componentDidMount,
					a = e.componentWillUnmount;
				let i;
				e.componentDidMount = function() {
					i = s.b.subscribe(() => {
						const e = s.b.getState();
						n.forEach(n => {
							const s = t[n](e);
							s instanceof Promise ? s.then(e => {
								this.state[n] !== e && this.setState({
									[n]: e
								})
							}, e => {
								console.log(e)
							}) : this.state[n] !== s && this.setState({
								[n]: s
							})
						})
					}), o && o.call(e)
				}, e.componentWillUnmount = function() {
					i(), a && a.call(e)
				}
			}
		},
		340: function(e, t, n) {
			var s = n(341);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		341: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".logo{display:flex;align-items:center;max-height:100%}.logo>img{margin-right:10px}.logo .etch{color:#FAC000}.logo .psd{color:#4f85f2}\n", ""])
		},
		342: function(e, t, n) {
			var s = n(343);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		343: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, "nav.navbar{display:flex;min-height:4rem;align-items:stretch;position:relative}nav.navbar>.title{position:absolute;left:50%;top:50%;-webkit-transform:translate(-50%, -50%);transform:translate(-50%, -50%);font-size:1.1em;color:#e6e6e6}nav.navbar .navbar-item{display:flex;align-items:center}nav.navbar .tools{align-self:center;display:flex;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;align-items:center;font-size:1.2em;margin-right:12px}nav.navbar .tools .text{font-size:0.8em;font-weight:bold;margin-right:5px}nav.navbar .tools .zoom{margin-left:8px;margin-right:8px;cursor:pointer;font-size:1.2em}nav.navbar .tools .zoomLevel{width:36px;text-align:center}nav.navbar .pillars{display:flex}nav.navbar .pillars .pillar{display:flex;align-items:center;font-weight:bold;margin-left:5px;padding-left:5px;margin-right:5px;padding-right:5px;cursor:pointer;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;color:#b3b3b3}nav.navbar .pillars .pillar.active{color:#fff;border-bottom:2px solid #999}nav.navbar .pillars .pillar:hover{background:rgba(255,255,255,0.1)}nav.navbar .navbar-end .navbar-item>.icon{margin-right:8px}nav.navbar .navbar-end .navbar-item.ext{color:#1abc9c}nav.navbar .navbar-end .navbar-item.bug{color:#e74c3c}nav.navbar .navbar-end .share-dropdown{width:400px}nav.navbar .userInfo{padding-top:0 !important;padding-bottom:0 !important;align-items:center}nav.navbar .userInfo>.profile{height:100%;margin-right:0.5rem}nav.navbar .userInfo>.profile>img{max-height:60px;height:60px}nav.navbar .userInfo>.name_plan>.plan{text-transform:capitalize}\n", ""])
		},
		344: function(e, t, n) {
			var s = n(345);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		345: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".fileDropper{text-align:center;display:flex;flex-direction:column;align-items:center;cursor:pointer;transition:all 300ms ease-out;justify-content:center;position:relative}.fileDropper.dragging{background:rgba(0,0,0,0.08)}.fileDropper.dragging>.browseFile{font-size:0px;padding:0;border:none}.fileDropper.dragging>.or{font-size:0px}.fileDropper.dragging>button{opacity:0}.fileDropper>*{margin-bottom:0.5em;pointer-events:none;transition:all 300ms ease-out}.fileDropper>.browseFile{font-size:1.2em;font-weight:bold;color:#57C4FA;padding:1em;border:3px solid #57C4FA;border-radius:0.7em}.fileDropper>.loadedFile{color:#428CD0}.fileDropper>.formatsContainer{position:absolute;top:20px;right:20px}\n", ""])
		},
		346: function(e, t, n) {
			var s = n(347);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		347: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".extensionView .fileDropperWrapper{border:1px dashed  #5e6d6f;padding:15px}.extensionView .installed{margin-bottom:1em}.extensionView .installed>h3{font-weight:bold;font-size:1.1em;margin-bottom:1em}.extensionView .installed .no-ext{background:#212727;text-align:center;font-size:1.2em;font-weight:lighter;padding:1em}.extensionView .installed .extList{background:#212727;display:flex;flex-wrap:wrap;padding:1em}.extensionView .installed .extList .extItem{padding:0.5em;margin-right:20px;display:flex;align-items:center;background:#282f2f;border-radius:8px}.extensionView .installed .extList .extItem .name{margin-right:10px}.extensionView .installed .extList .extItem .delIcon{font-size:1.3em;cursor:pointer}.extensionView .installed .extList .extItem .delIcon:hover{color:#e74c3c}\n", ""])
		},
		348: function(e, t, n) {
			var s = n(349);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		349: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".psdetch-modal .modal-card-foot{justify-content:flex-end}\n", ""])
		},
		350: function(e, t, n) {
			var s = n(351);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		351: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".footer .sec{margin-left:0.5rem}.footer .v2{float:right}\n", ""])
		},
		352: function(e, t, n) {
			var s = n(353);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		353: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".supportFormats>p{text-align:left;margin-top:1rem;margin-bottom:1rem}.supportFormats .adapter{display:flex;align-items:center;margin-bottom:1rem;padding-left:1rem}.supportFormats .icon{width:24px;margin-right:1rem}\n", ""])
		},
		354: function(e, t, n) {
			var s = n(355);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		355: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".app{display:flex;flex-direction:column;height:100%;max-height:100%;min-width:1300px;min-height:720px;position:relative}.app>.fileDropper{flex:1 1 auto}.app>.loading{display:flex;align-items:center;justify-content:center;flex:1 1 auto}.app>.loading>i{margin-right:12px}.app .welcome{flex:1;position:relative}.app .welcome .recentPostsWrapper{position:absolute;left:10px;top:10px;width:20%;bottom:10px}.app .welcome .welcome-content{margin-left:auto;margin-right:auto;margin-top:30px;display:flex;flex-direction:column;align-items:center}@media screen and (min-height: 1000px){.app .welcome .welcome-content{margin-top:80px}}.app .welcome .welcome-content>div{margin-bottom:2rem}.app .welcome .welcome-content .logo{font-size:4rem;text-shadow:0 0 15px #efefef;-webkit-animation-iteration-count:infinite;animation-iteration-count:infinite;-webkit-animation-name:glow;animation-name:glow;-webkit-animation-duration:5s;animation-duration:5s}.app .welcome .welcome-content .logo>img{width:64px}.app .welcome .welcome-content .button{font-size:1.4rem}.app .welcome .patreon-wrapper{font-weight:lighter;position:absolute;width:400px;bottom:15px;right:15px}.app .welcome .btns{margin-top:15px;display:flex;justify-content:space-around;color:white !important}.app .welcome .btns .twitter-share-button{border-radius:9999px;padding:0.5rem;padding-left:1rem;padding-right:2rem;background:#1DA1F2;display:flex;align-items:center;text-align:center;text-transform:none;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;white-space:unset;justify-content:center;text-decoration:none !important;cursor:pointer}.app .welcome .btns .twitter-share-button>.icon{margin-right:10px;width:22px;height:22px}.app .welcome .patreon-btn-real{width:180px;background-color:#e85b46;cursor:pointer;position:relative;font-size:0.875rem;border-radius:9999px;border:1px solid #e85b46;font-weight:500}.app .welcome .patreon-btn-real>a{padding:0.46875rem 1rem;pointer-events:unset;text-align:center;text-transform:none;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;white-space:unset;display:flex;align-items:center;justify-content:center;text-decoration:none !important}.app .welcome .patreon-btn-real>a>.text{margin-left:0.5rem}.app .welcome .patreon-btn-real .icon{width:1rem;height:1rem}.app .welcome .patreon-btn-real .icon>svg{fill:white;width:1rem;height:1rem}@-webkit-keyframes glow{0%{text-shadow:0 0 30px #efefef}50%{text-shadow:0 0 5px #efefef}100%{text-shadow:0 0 30px #efefef}}@keyframes glow{0%{text-shadow:0 0 30px #efefef}50%{text-shadow:0 0 5px #efefef}100%{text-shadow:0 0 30px #efefef}}\n", ""])
		},
		356: function(e, t, n) {
			var s = n(357);
			"string" == typeof s && (s = [
				[e.i, s, ""]
			]);
			var o = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			n(21)(s, o);
			s.locals && (e.exports = s.locals)
		},
		357: function(e, t, n) {
			(e.exports = n(20)(!1))
			.push([e.i, ".recentPosts{display:flex;flex-direction:column;position:absolute;top:0;bottom:0;left:0;right:0;overflow:auto;max-width:350px}.recentPosts .loading{position:absolute;top:0;bottom:0;left:0;right:0;display:flex;align-items:center;justify-content:center;flex-direction:column}.recentPosts .posts>.title{font-size:1.5em;font-weight:bold}.recentPosts .posts .card{margin-bottom:1em}.recentPosts .posts .card-content{padding:0.6em}.recentPosts .posts .card-content>*{margin-bottom:0.3em}.recentPosts .posts .card-content .title{font-size:1.2em;font-weight:bold}.recentPosts .posts .card-content .subtitle{font-size:0.9em}.recentPosts .posts .card-content .readmore{text-align:right}\n", ""])
		},
		358: function(e, t, n) {
			e.exports = n.p + "89eaa7422b26701cdfabbdf4fde0e5ee.svg"
		},
		55: function(e, t, n) {
			"use strict";
			n.r(t), n.d(t, "pagesToLayout", (function() {
				return o
			})), n.d(t, "nextGridSlot", (function() {
				return a
			})), n.d(t, "scalePageLayouts", (function() {
				return i
			}));
			var s = n(1);

			function o(e) {
				let t;
				const n = [],
					o = [],
					i = [],
					r = [];
				if (e.forEach(e => {
					void 0 !== e.offsetX && void 0 !== e.offsetY ? i.push(e) : r.push(e)
				}), i.forEach(e => {
					let n = new s.Rect(e.offsetX, e.offsetY, e.offsetX + e.width, e.offsetY + e.height);
					t = t ? t.combine(n) : n, o.push({
						page: e,
						rect: n
					})
				}), t && (0 !== t.left || 0 !== t.top)) {
					for (const e of o) e.rect = e.rect.pan(-t.left, -t.top);
					t = t.pan(-t.left, -t.top)
				}
				return r.forEach(e => {
					const i = a(n);
					t && (i.y += t.height + 20);
					let r = new s.Rect(i.x, i.y, e.width, e.height);
					n.push(r), o.push({
						page: e,
						rect: r
					})
				}), o
			}

			function a(e) {
				const t = Math.floor(e.length / 4),
					n = e.length % 4,
					s = e.slice(4 * t)
					.reduce((e, t) => e + t.width + 20, 0);
				let o = 0;
				if (n > 0) o = e[4 * t + n - 1].top;
				else
					for (let n = 0; n < t; n++) {
						for (let t = 0; t < 4; t++) {
							const s = e[4 * n + t];
							o = Math.max(o, s.top + s.height)
						}
						o += 20
					}
				return {
					x: s,
					y: o
				}
			}

			function i(e, t) {
				return 1 === t ? e : e.map(e => ({
					page: e.page,
					rect: e.rect.zoom(t)
				}))
			}
		},
		898: function(e, t, n) {
			"use strict";
			n.r(t);
			var s = n(4);
			n(340);
			class o extends s.a {
				render() {
				
				}
			}
			n(342);
			var a = n(33),
				i = n(0),
				r = n(106);
			async function c(e, t) {
				const n = Object.keys(t),
					s = n.map(e => t[e]),
					o = await Promise.all(s),
					a = {};
				for (let e = 0; e < n.length; e++) {
					const t = n[e],
						s = o[e];
					a[t] = s
				}
				e.setState(a)
			}
			var l = n(159);
			n(346);
			class p extends s.a {
				async removeExt(e) {
					await r.extManager.deleteExtension(e.id), c(this, {
						extensions: r.extManager.listExtensions()
					})
				}
				constructor() {
					super(), this.state = {
						extensions: []
					}
				}
				componentWillMount() {
					c(this, {
						extensions: r.extManager.listExtensions()
					})
				}
				renderExtensionList() {
					return Object(s.b)("div", {
						class: "extList"
					}, this.state.extensions.map(e => Object(s.b)("div", {
						class: "extItem"
					}, Object(s.b)("span", {
						class: "name"
					}, e.name), Object(s.b)("span", {
						class: "delIcon",
						onClick: () => this.removeExt(e)
					}, Object(s.b)("i", {
						class: "fas fa-trash-alt"
					})))))
				}
				async onExtFile(e) {
					await r.extManager.addExtension(e.name, e), c(this, {
						extensions: r.extManager.listExtensions()
					})
				}
				render() {
					return Object(s.b)("div", {
						class: "modal is-active extensionView"
					}, Object(s.b)("div", {
						class: "modal-background",
						onClick: () => this.props.onClose()
					}), Object(s.b)("div", {
						class: "modal-card"
					}, Object(s.b)("header", {
						class: "modal-card-head"
					}, Object(s.b)("p", {
						class: "modal-card-title"
					}, "Extensions")), Object(s.b)("section", {
						class: "modal-card-body"
					}, Object(s.b)("div", {
						class: "installed"
					}, Object(s.b)("h3", null, "Installed"), this.state.extensions.length > 0 ? this.renderExtensionList() : Object(s.b)("p", {
						class: "no-ext"
					}, "No Extension Installed")), Object(s.b)("div", {
						class: "fileDropperWrapper"
					}, Object(s.b)(l.a, {
						title: "Drag Extension Here",
						onFile: e => this.onExtFile(e)
					}))), Object(s.b)("footer", {
						class: "modal-card-foot"
					}, Object(s.b)("button", {
						onClick: () => this.props.onClose(),
						class: "button"
					}, "Close"))))
				}
			}
			var d = n(9);
			class b extends s.a {
				constructor() {
					super(), Object(a.b)(this, {
						loaded: e => !!e.sessionState.project,
						title: e => e.sessionState.project ? e.sessionState.project.name : "",
						user: e => e.sessionState.user
					}), this.state = {
						title: "",
						loaded: !1,
						showExtensionModal: !1
					}
				}
				render() {
					return Object(s.b)("nav")
				}
				renderUserInfo() {
				
				}
				closeProject() {
					i.b.dispatch({
						type: "closeProject"
					})
				}
				renderRightTools() {
					return Object(s.b)("div", {
						class: "navbar-end"
					}, Object(s.b)("a", {
						onClick: () => {
							this.closeProject()
						},
						class: "navbar-item is-size-4 "
					}, Object(s.b)("i", {
						class: "fas fa-times"
					})))
				}
				renderNavTools() {
					return []
				}
			}
			var u = n(323),
				f = (n(350), n(160));
			class g extends s.a {
				render() {
				
				}
			}
			var h = n(161),
				m = n(162),
				x = (n(354), n(6));
			async function A() {
				
				
			}
			class v {
				constructor(e) {
					const t = e.split("\n"),
						n = t.shift();
					n && (n.indexOf("<img") > -1 ? this.img = n : this.excerpt = n, t.length && (this.excerpt = t.join("")))
				}
			}
			n(356);
			var w = n(31);
			class j extends s.a {
				constructor() {
					super()
				}
				componentDidMount() {
					this.loadPosts()
				}
				async loadPosts() {
					await Object(w.sleep)(1e3), c(this, {
						rencentPosts: A()
					})
				}
				renderPosts() {
					const e = this.state.rencentPosts;
					return Object(s.b)("div")
				}
				render() {
					return Object(s.b)("div")
				}
			}
			var O = n(358),
				y = n.n(O);
			n.d(t, "App", (function() {
				return D
			}));
			const C = n(160);
			class D extends s.a {
				constructor() {
					super(), this.state = {
						content: this.renderDefaultContent(),
						showFooter: !0
					}
				}
				async loadInspectStudio() {
					Object(x.a)("v" + C.version);
					const {
						InspectStudio: e
					} = await n.e(14)
						.then(n.bind(null, 897));
					this.setState({
						content: Object(s.b)(e, null),
						showFooter: !1
					})
				}
			
				renderDefaultContent() {
					 this.loadInspectStudio()
				}
				render() {
				
					return Object(s.b)("div", {
						class: "app"
					}, Object(s.b)(b, null), this.state.content, this.state.showFooter && Object(s.b)(g, null), Object(s.b)(u.a, null))
				}
			}
		}
	}
]);