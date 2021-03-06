/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
<template>
    <div class="contract-content">
        <v-content-head :headTitle="$t('route.contractManagementQ')" :headSubTitle="$t('route.contractIDE')" @changeGroup="changeGroup"></v-content-head>
        <div class="code-menu-wrapper" :style="{width: menuWidth+'px'}">
            <v-menu @change="changeCode($event)" ref="menu" v-show="menuHide"></v-menu>
            <div class="move" @mousedown="dragDetailWeight($event)"></div>
        </div>
        <div :class="[!menuHide ?  'code-detail-wrapper' : 'code-detail-reset-wrapper']" :style="{width: contentWidth}">
            <v-code :changeStyle="changeWidth" :data="contractData" :show="showCode" @add="add($event)" @compile="compile($event)" @deploy="deploy($event)"></v-code>
        </div>
    </div>
</template>

<script>
import menu from "./components/contractCatalog";
import codes from "./components/code";
import contentHead from "@/components/contentHead";
import { encryption } from "@/util/api";
import Bus from "@/bus"
export default {
    name: "contract",
    components: {
        "v-menu": menu,
        "v-code": codes,
        "v-content-head": contentHead
    },
    watch: {
        $route: function () {
            this.urlQuery = this.$root.$route.query;
        },
        menuHide: function (val) {
            if (val) {
                this.menuWidth = 180;
            } else {
                this.menuWidth = 0;
            }
        }
    },
    data: function () {
        return {
            contractData: {},
            showCode: false,
            menuHide: true,
            changeWidth: false,
            contractHide: false,
            menuWidth: 240,
            urlQuery: this.$root.$route.query
        };
    },
    computed: {
        contentWidth: function () {
            if (this.menuWidth) {
                return `calc(100% - ${this.menuWidth}px)`;
            } else {
                return `100%`;
            }
        }
    },
    beforeDestroy: function () {
        Bus.$off("changeGroup")
    },
    mounted: function () {
        Bus.$on("changeGroup", data => {
            this.changeGroup()
        })
        this.getEncryption(this.initSolc);
    },
    methods: {
        initSolc() {
            var head = document.head;
            var script = document.createElement("script");
            if (localStorage.getItem("encryptionId") == 0) {
                script.src = "./static/js/soljson-v0.4.25+commit.59dbf8f1.js";
            } else {
                script.src = "./static/js/soljson-v0.4.25-gm.js";
            }
            script.setAttribute('id', 'soljson');
            if (!document.getElementById('soljson')) {
                head.append(script)
            }
        },
        getEncryption: function (callback) {
            encryption().then(res => {
                if (res.status == 200) {
                    localStorage.setItem("encryptionId", res.data)
                    callback();
                } else {
                    this.$message({
                        type: "error",
                        message: this.$chooseLang(res.data.code)
                    });
                }
            })
                .catch(err => {
                    this.$message({
                        type: "error",
                        message: this.$t('text.systemError')
                    });
                });
        },
        changeGroup: function () {
            this.$refs.menu.getContracts()
        },
        dragDetailWeight: function (e) {
            let startX = e.clientX,
                menuWidth = this.menuWidth;
            document.onmousemove = e => {
                let moveX = e.clientX - startX;
                if (menuWidth + moveX > 180) {
                    this.menuWidth = menuWidth + moveX;
                }
            };

            document.onmouseup = e => {
                document.onmousemove = null;
                document.onmouseup = null;
            };
        },
        changeCode: function (val) {
            this.contractData = val;
            this.showCode = true;
        },
        add: function (val) {
            this.$refs.menu.saveContact(val);
        },
        compile: function (val) {
            this.$refs.menu.saveContact(val);
        },
        deploy: function (val) {
            this.$refs.menu.saveContact(val);
        }
    }
};
</script>
<style scoped>
.contract-content {
    width: 100%;
    height: 100%;
    font-size: 0;
}
.contract-content::after {
    display: block;
    content: "";
    clear: both;
}
.code-menu-wrapper {
    float: left;
    position: relative;
    padding-left: 20px;
    height: calc(100% - 57px);
    font-size: 12px;
    box-sizing: border-box;
}
.move {
    position: absolute;
    top: 0;
    left: 98%;
    width: 3px;
    height: 100%;
    z-index: 9999;
    cursor: w-resize;
}
.menu-spread {
    position: relative;
    width: 31px;
    height: 47px;
    line-height: 47px;
    border: 1px solid #e7ebf0;
    border-bottom: 2px solid #e7ebf0;
    color: #aeb1b5;
    background-color: #fff;
    text-align: center;
    cursor: pointer;
}
.menu-spread i {
    font-size: 12px;
}
.code-detail-wrapper {
    float: left;
    height: calc(100% - 57px);
    padding-right: 20px;
    font-size: 12px;
    box-sizing: border-box;
}
.code-detail-reset-wrapper {
    float: left;
    height: calc(100% - 57px);
    padding-right: 20px;
    font-size: 12px;
    box-sizing: border-box;
}
.menu-drag {
    position: absolute;
    width: 36px;
    height: 36px;
    line-height: 32px;
    border: 1px solid #e8e8e8;
    left: 370px;
    top: 50%;
    background-color: #fff;
    cursor: pointer;
}
</style>