<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import {get} from "@/net/index.js";
import router from "@/router/index.js";
const counts=ref(0);
function ShowCounts() {
  if (counts.value < 5) {
    counts.value++;
    return true;
  } else {
    return false;
  }
}
const options=reactive({
  blogs:[],
  AuthorBlogs:[],
  SearchBlogs:[],
  recommendBlogs:[],
})
const option_link=reactive({
  links:[]
})
const initDate=()=>{
  get('/api/blog/getIntroduce',{},(message,data)=>{
    options.blogs=data;
  })
}
const initLink=()=>{
  get('/api/link/getAllLinks',{},(message,data)=>{
    option_link.links=data;
  })
}
const AuthorBlogs=()=>{
  get('/api/blog/getBlogsByAuthorId',{},(message,data)=>{
    options.AuthorBlogs=data;
  })
}
const showForm = ref(false);
const blogClickHandle=(id)=>{
  router.push("/view/"+id)
}

//搜索
const IsSearch=ref(false);
const searchQuery = reactive({
  search:'',
});

const search =  () => {
  get("api/blog/SearchBlogs",{
    title: searchQuery.search,
  },(message,data) => {
    console.log(searchQuery.search)
    IsSearch.value=true;
    options.SearchBlogs = data;
  })
};
// 监听searchQuery.search的变化
watch(() => searchQuery.search, (newValue, oldValue) => {
  if (newValue !== oldValue) {
    search();
  }
});

//推荐的Blog
const getRecommendBlogs=()=>{
  get('/api/blog/getBlogsOrderByWatches',{},(message,data)=>{
    options.recommendBlogs=data;
  })
}
const SuggestionBar=[{
  title:'火箭',
  ico:`<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 64 64" class="size-7 text-pink-300">
<path fill="pink" d="m61.4 19.5l-8.2-8.2l-.3-.3q-.9-.6-1.8-.6l-12.9 1.1C28.5 3.4 16.3-.1 6 2.7C4.4 3.1 3.1 4.4 2.7 6c-2.8 10.2.7 22.5 8.9 32.1l-1.1 12.7v.2c0 .6.2 1.3.6 1.8l8.5 8.6q.9.9 2.1.9c.3 0 .6 0 .8-.1c1-.3 1.8-1.2 2.1-2.2l1.2-10.7q2.1 1.05 4.2 1.8q.75.3 1.5.3c1.3 0 2.5-.5 3.4-1.4l14.9-14.9c1.3-1.3 1.8-3.3 1.2-5q-.75-2.1-1.8-4.2l10.5-1.2h.2c1.1-.2 1.9-1 2.2-2.1c.3-1.2.1-2.3-.7-3.1M20.4 56L15 50.5l.7-8.1c1.8 1.6 3.7 3.1 5.7 4.4zm26.2-24.2L31.8 46.6c-.1.1-.2.1-.3.1c-5.2-1.8-10.2-5-14.3-9.1C8.4 28.7 4.4 16.8 7 7.2c.1-.1.1-.2.2-.2c2-.5 4-.8 6.1-.8c8.3 0 17.3 3.9 24.2 10.9c4.1 4.1 7.3 9.1 9.1 14.3c.1.2.1.3 0 .4m.2-10.3c-1.3-2-2.8-4-4.4-5.8l8.1-.7l5.4 5.4zm8.5 18.7c-.3-.2-1.4-.7-2.6.3c-1 .8-5.7 5.5-6.6 6.4c-6.4 6.4-6.4 6.4-6 7.8c.1.4.3.8.6 1c3.2 3.2 8.1 3.9 11.8 3.9c2.8 0 4.9-.4 5.1-.5c.9-.2 1.6-.9 1.8-1.8c.1-.5 2.2-11.2-3.5-16.9c-.3 0-.5-.1-.6-.2m-.2 14.9c-2.6.3-6.6.3-9.6-1.2c2.2-2.2 6.1-6.2 8.3-8.3c1.5 2.8 1.5 6.9 1.3 9.5"/><path fill="currentColor" d="M26.1 17.8c-4.6 0-8.3 3.7-8.3 8.3s3.7 8.3 8.3 8.3s8.3-3.7 8.3-8.3s-3.7-8.3-8.3-8.3m0 12.2c-2.1 0-3.8-1.7-3.8-3.8s1.7-3.8 3.8-3.8S30 24 30 26.1S28.2 30 26.1 30"/>
</svg>`
},{
  title:'宇宙',
  ico:`<svg xmlns="http://www.w3.org/2000/svg"  viewBox="-3 -3 24 24" class="size-7 text-yellow-500">
<path fill="currentColor" d="M.694 17.306c-1.91-1.912.258-7.18 4.845-11.767S15.394-1.217 17.306.694c1.91 1.912-.258 7.18-4.845 11.767S2.606 19.217.694 17.306M2.08 15.92c.815.816 5.102-.95 8.997-4.845s5.66-8.182 4.845-8.997c-.815-.816-5.102.95-8.997 4.845s-5.66 8.182-4.845 8.997m3.46-3.46C.952 7.874-1.217 2.606.694.694c1.912-1.91 7.18.258 11.767 4.845s6.756 9.855 4.845 11.767c-1.912 1.91-7.18-.258-11.767-4.845zm1.385-1.385c3.895 3.895 8.182 5.66 8.997 4.845c.816-.815-.95-5.102-4.845-8.997S2.895 1.263 2.08 2.078c-.816.815.95 5.102 4.845 8.997M9 11a2 2 0 1 1 0-4a2 2 0 0 1 0 4"/>
</svg>`
},{
  title:'火焰',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" class="size-7 text-red-800 flame" viewBox="0 0 20 20"><g fill="currentColor" fill-rule="evenodd" clip-rule="evenodd">
<path d="M6.96 13.1a.9.9 0 0 1 .9.9a2.14 2.14 0 0 0 4.28 0a.9.9 0 0 1 1.8 0a3.94 3.94 0 0 1-7.88 0a.9.9 0 0 1 .9-.9Z"/><path d="M10.043 9.693a.9.9 0 0 1 1.223-.35c1.482.824 2.165 2 2.463 2.978c.146.482.199.91.215 1.223a4.298 4.298 0 0 1-.007.533l-.001.013v.005l-.001.002v.001L13.04 14l.895.1a.9.9 0 0 1-1.79-.192m0 0v-.005l.003-.047a3.319 3.319 0 0 0-.141-1.01c-.187-.612-.61-1.37-1.615-1.929a.9.9 0 0 1-.35-1.224m-1.192.523a.9.9 0 0 0-1.273-.024a4.947 4.947 0 0 0-1.417 2.561a5.034 5.034 0 0 0-.105 1.213l.006.087l.002.029l.001.01v.004l.001.002l.874-.096l-.874.097a.9.9 0 0 0 1.79-.191m0-.002v.002v-.002Zm0-.004v.004a2.205 2.205 0 0 1-.004-.186c.003-.142.019-.35.07-.594a3.147 3.147 0 0 1 .904-1.637a.9.9 0 0 0 .024-1.273m-.995 3.685Z"/><path d="M7.63 10.145a.9.9 0 0 1 1.267.12l1.244 1.503a.9.9 0 0 1-1.387 1.147L7.51 11.412a.9.9 0 0 1 .12-1.267Z"/><path d="M11.306 9.367a.9.9 0 0 1 .286 1.24L10.21 12.82a.9.9 0 0 1-1.526-.954l1.382-2.211a.9.9 0 0 1 1.24-.287Z"/><path d="M3.5 10a1 1 0 0 1 1 1c0 3.09 2.437 5.568 5.498 5.568c3.062 0 5.502-2.48 5.502-5.568a1 1 0 1 1 2 0c0 4.166-3.309 7.568-7.502 7.568S2.5 15.166 2.5 11a1 1 0 0 1 1-1Z"/><path d="M6.1 4.7a1 1 0 0 0-1.4.2C3.055 7.094 2.5 9.81 2.5 11a1 1 0 1 0 2 0c0-.81.445-3.094 1.8-4.9a1 1 0 0 0-.2-1.4Zm7.26-.968a1 1 0 0 1 1.408.128c1.372 1.646 2.052 3.421 2.39 4.772c.17.678.255 1.256.298 1.67a8.929 8.929 0 0 1 .043.635v.042l.001.013v.007l-1 .001h1a1 1 0 0 1-2 .003v-.02l-.004-.091a6.936 6.936 0 0 0-.03-.382a9.971 9.971 0 0 0-.249-1.392c-.287-1.15-.857-2.624-1.985-3.978a1 1 0 0 1 .128-1.408Z"/><path d="M4.985 4.643a1 1 0 0 1 1.372.343l1.5 2.5a1 1 0 0 1-1.715 1.028l-1.5-2.5a1 1 0 0 1 .343-1.372Zm3.803-3.346a1 1 0 0 1 1.415-.009L9.5 2l.703-.712l.002.003l.004.004l.015.014l.053.054a20.708 20.708 0 0 1 .786.844c.439.5 1.019 1.21 1.334 1.852c.255.516.518 1.178.712 1.69a38.236 38.236 0 0 1 .31.852l.02.053l.006.02a1 1 0 0 1-1.89.652l-.002-.004l-.004-.012l-.017-.048a32.959 32.959 0 0 0-.293-.804c-.19-.502-.426-1.09-.636-1.516c-.188-.381-.607-.92-1.043-1.415a18.716 18.716 0 0 0-.706-.758l-.044-.045l-.01-.01l-.002-.002a1 1 0 0 1-.01-1.415Z"/><path d="M9.545 1.225c.57.08.966.605.886 1.174l-1.03-.144l1.03.144v.003l-.001.006l-.003.022l-.012.077c-.01.066-.024.16-.044.275c-.038.23-.096.55-.171.905c-.144.676-.377 1.602-.716 2.263a21.67 21.67 0 0 1-.968 1.645a40.147 40.147 0 0 1-.517.788l-.033.049l-.012.017a1.04 1.04 0 0 1-1.718-1.172l.002-.003l.007-.01l.03-.045a30.657 30.657 0 0 0 .488-.744c.3-.47.644-1.032.87-1.473c.201-.392.39-1.077.532-1.748a19.453 19.453 0 0 0 .194-1.06l.01-.064l.002-.015V2.11a1.04 1.04 0 0 1 1.174-.886Zm4.969 2.418a1 1 0 0 0-1.371.343l-1.5 2.5a1 1 0 1 0 1.714 1.028l1.5-2.5a1 1 0 0 0-.342-1.372Z"/></g>
</svg>`
},{
  title:'星星火箭',
  ico:`<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 14 14" class="size-7 text-green-600"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round">
<path d="M6.022 4.347a18.5 18.5 0 0 0-1.93 1.686C1.248 8.877-.192 12.046.874 13.113c1.066 1.066 4.236-.375 7.079-3.218a18.5 18.5 0 0 0 1.686-1.931"/><path d="M9.639 7.964c1.677 2.226 2.36 4.32 1.532 5.148c-1.067 1.067-4.236-.374-7.08-3.217C1.249 7.05-.191 3.882.875 2.815c.828-.827 2.922-.144 5.148 1.532"/><path d="M5.522 7.964a.5.5 0 1 0 1 0a.5.5 0 0 0-1 0m2.51-4.354c-.315-.055-.315-.506 0-.56a2.84 2.84 0 0 0 2.29-2.193L10.34.77c.068-.31.51-.312.58-.003l.024.101a2.86 2.86 0 0 0 2.296 2.18c.316.055.316.509 0 .563a2.86 2.86 0 0 0-2.296 2.18l-.024.101c-.07.31-.512.308-.58-.002l-.02-.087A2.84 2.84 0 0 0 8.03 3.61Z"/></g>
</svg>`
},{
  title:'圆形火焰',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" class="size-7 text-red-700" viewBox="0 0 26 26"><g fill="currentColor">
<path fill="white" d="M13.5 26C20.404 26 26 20.404 26 13.5S20.404 1 13.5 1S1 6.596 1 13.5S6.596 26 13.5 26m0-2C19.299 24 24 19.299 24 13.5S19.299 3 13.5 3S3 7.701 3 13.5S7.701 24 13.5 24" clip-rule="evenodd" opacity=".2"/><g opacity=".2"><path d="M14.498 21.568C18.126 21.568 21 18.628 21 15c0 0 0-3.416-2.41-6.39a.1.1 0 0 0-.162.01l-1.318 2.196a.101.101 0 0 1-.181-.018c-.161-.452-.567-1.562-.929-2.298c-.411-.836-1.489-1.978-1.867-2.365a.099.099 0 0 0-.168.05c-.111.575-.48 2.343-.965 3.315c-.477.953-1.17 2.01-1.413 2.371a.1.1 0 0 1-.167-.004l-1.343-2.24a.1.1 0 0 0-.165-.007C8.48 11.599 8 14.02 8 15c0 3.627 2.87 6.568 6.498 6.568"/><path fill-rule="evenodd" d="M13.474 6.09a.599.599 0 0 1 1.017-.304c.194.198.567.59.946 1.042c.372.443.778.976 1.012 1.451c.228.464.47 1.064.661 1.565l.89-1.48a.6.6 0 0 1 .98-.07c1.263 1.56 1.892 3.231 2.205 4.507c.157.639.236 1.182.276 1.568a8 8 0 0 1 .04.58V15H21h.5c0 3.897-3.091 7.07-7.002 7.07S7.5 18.896 7.5 14.998c0-1.073.507-3.6 2.007-5.672a.6.6 0 0 1 .998.043l1.015 1.69c.308-.482.72-1.159 1.033-1.784c.45-.9.807-2.597.921-3.185" clip-rule="evenodd"/></g><path fill-rule="evenodd" d="M16.09 17.024a4.61 4.61 0 0 0-2.377-4.279a.6.6 0 0 0-.809.201l-.964 1.543l-.802-.97a.5.5 0 0 0-.734-.039a4.6 4.6 0 0 0-1.395 3.544a3.54 3.54 0 0 0 7.08 0m-5.353-2.42l-.062.082a3.6 3.6 0 0 0-.669 2.26l.003.059a2.54 2.54 0 0 0 5.08 0l.003-.058c.007-.065.163-1.853-1.473-3.069l-.081-.059l-1.044 1.67a.6.6 0 0 1-.971.064z" clip-rule="evenodd"/><path fill-rule="evenodd" d="M11.974 5.09a.599.599 0 0 1 1.017-.304c.194.198.567.59.946 1.042c.372.443.778.976 1.012 1.451c.228.464.47 1.064.661 1.565l.889-1.48a.6.6 0 0 1 .98-.07c1.263 1.56 1.892 3.231 2.205 4.507c.157.639.236 1.182.276 1.568a8 8 0 0 1 .04.58V14c0 3.897-3.091 7.07-7.002 7.07S6 17.896 6 13.998c0-1.073.507-3.6 2.007-5.672a.6.6 0 0 1 .998.043l1.015 1.69c.308-.482.72-1.159 1.033-1.784c.45-.9.807-2.597.921-3.185m.823.95c-.181.8-.48 1.942-.85 2.684c-.492.983-1.201 2.063-1.445 2.426a.6.6 0 0 1-1.01-.026l-1.02-1.697C7.376 11.2 7 13.193 7 14c0 3.358 2.654 6.068 5.998 6.068S19 17.358 19 14v-.028l-.004-.1a8 8 0 0 0-.031-.401a11 11 0 0 0-.252-1.431a10.7 10.7 0 0 0-1.66-3.655l-1.014 1.688a.601.601 0 0 1-1.081-.108c-.162-.454-.559-1.538-.907-2.244c-.177-.361-.516-.817-.88-1.25a17 17 0 0 0-.374-.43" clip-rule="evenodd"/><path fill-rule="evenodd" d="M13 24.5c6.351 0 11.5-5.149 11.5-11.5S19.351 1.5 13 1.5S1.5 6.649 1.5 13S6.649 24.5 13 24.5m0 1c6.904 0 12.5-5.596 12.5-12.5S19.904.5 13 .5S.5 6.096.5 13S6.096 25.5 13 25.5" clip-rule="evenodd"/></g>
</svg>`
},{
  title:'星星',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" class="size-7" viewBox="0 0 48 48">
<path fill="#45413c" d="M9.5 45.75a14.5 1.25 0 1 0 29 0a14.5 1.25 0 1 0-29 0" opacity=".15"/><path fill="#ffe500" d="M33.7 30.66c.47-1.51 2.06-4 4.08-4.33l8.22-1.4a.57.57 0 0 0 .53-.49A.56.56 0 0 0 46 24l-8.19-1.41c-2-.34-3.61-2.81-4.08-4.32L31.81 7.59a.75.75 0 0 0-1.32 0L28.6 18.22c-.47 1.51-2.05 4-4.08 4.32L16.33 24a.55.55 0 0 0-.52.49a.56.56 0 0 0 .52.49l8.19 1.4c2 .35 3.61 2.82 4.08 4.33l1.89 10.68a.75.75 0 0 0 1.32 0Z"/><path fill="#fff48c" d="m46 24l-8.19-1.41c-2-.34-3.61-2.81-4.08-4.32L31.81 7.59a.75.75 0 0 0-1.32 0L29.88 11l1.82 10.28c.47 1.51 2.06 4 4.08 4.32l3.14.54l7-1.21a.57.57 0 0 0 .53-.49A.56.56 0 0 0 46 24"/><path fill="none" stroke="#45413c" stroke-linecap="round" stroke-linejoin="round" d="M33.7 30.66c.47-1.51 2.06-4 4.08-4.33l8.22-1.4a.57.57 0 0 0 .53-.49A.56.56 0 0 0 46 24l-8.19-1.41c-2-.34-3.61-2.81-4.08-4.32L31.81 7.59a.75.75 0 0 0-1.32 0L28.6 18.22c-.47 1.51-2.05 4-4.08 4.32L16.33 24a.55.55 0 0 0-.52.49a.56.56 0 0 0 .52.49l8.19 1.4c2 .35 3.61 2.82 4.08 4.33l1.89 10.68a.75.75 0 0 0 1.32 0Z"/><path fill="#ffe500" stroke="#45413c" stroke-linecap="round" stroke-linejoin="round" d="M14.62 16.27c.26-.82 1.13-2.17 2.24-2.36l4.49-.76c.17 0 .29-.14.29-.27s-.12-.24-.29-.27l-4.49-.76a3.54 3.54 0 0 1-2.24-2.35l-1-5.79a.36.36 0 0 0-.36-.21a.37.37 0 0 0-.36.21l-1 5.79a3.52 3.52 0 0 1-2.24 2.35l-4.48.76c-.17 0-.29.14-.29.27s.12.24.29.27l4.48.76c1.12.19 2 1.54 2.24 2.36l1 5.81a.36.36 0 0 0 .36.22a.35.35 0 0 0 .36-.22ZM10 34.83a2.39 2.39 0 0 1 1.54-1.59l3.08-.51a.19.19 0 1 0 0-.37l-3.08-.51A2.41 2.41 0 0 1 10 30.26l-.71-3.9a.29.29 0 0 0-.5 0l-.71 3.9a2.42 2.42 0 0 1-1.53 1.59l-3.09.51a.19.19 0 1 0 0 .37l3.08.51a2.39 2.39 0 0 1 1.54 1.59l.71 3.92a.29.29 0 0 0 .5 0Z"/>
</svg>`
},{
  title:'转的星星',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" class="size-7 text-blue-500" viewBox="0 0 14 14"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round">
<path d="M2.04 13.448v-2.48h2.48"/><path d="M13.339 5.815a6.449 6.449 0 0 1-11.3 5.308M.661 8.185a6.449 6.449 0 0 1 11.3-5.308"/><path d="M11.96.552v2.48H9.48M4.09 7.334c-.351-.061-.351-.565 0-.626a3.18 3.18 0 0 0 2.558-2.45l.021-.097c.076-.347.57-.349.649-.003l.026.113a3.19 3.19 0 0 0 2.565 2.435c.353.061.353.568 0 .63A3.19 3.19 0 0 0 7.344 9.77l-.026.112c-.079.346-.573.344-.649-.003l-.02-.096a3.18 3.18 0 0 0-2.56-2.45Z"/></g>
</svg>`
},{
  title:'龙',
  ico:`<svg xmlns="http://www.w3.org/2000/svg" class="size-7 text-yellow-800" viewBox="0 0 24 24">
<path fill="currentColor" d="M13.995 4.077v.06A11 11 0 0 1 16.22 8.42c.605 2.167.876 4.414.805 6.662a1 1 0 0 1 0 .113a6.3 6.3 0 0 0-.721-1.846a4.4 4.4 0 0 0-1.367-1.45c.025.064.059.113.059.168a7 7 0 0 1 .528 2.245a4.7 4.7 0 0 1-1.076 3.41a1.4 1.4 0 0 0-.113.203l-.069-.065a3.95 3.95 0 0 0-.138-2.374c-.578 2.216-2.068 3.538-4.047 4.437c2.404-.01 4.723-.375 6.697-1.856c.015.533-.148.958-.696 1.841c1.673-.839 2.403-2.23 2.586-4.012c.45.536.755 1.178.888 1.866c.128.656.192 1.327.286 1.974c.71-1.747.686-3.593.444-5.512c2.137 1.248 3.02 3.266 3.701 5.428a25 25 0 0 0-.133-3.494c-.17-1.698-.59-3.36-1.248-4.935a11.84 11.84 0 0 0-6.638-6.583a8.8 8.8 0 0 0-1.973-.563m-2.734.11c-.306.522-.587 1.06-.933 1.539a18 18 0 0 1-1.14 1.524a1.3 1.3 0 0 1-.943.43l.445-2.083a.6.6 0 0 0-.143.188c-.3.46-.592.928-.908 1.372a.63.63 0 0 1-.726.247c-.493-.144-.987-.282-1.48-.44a.26.26 0 0 0-.326.08A18 18 0 0 1 3.785 8.42c-1.076.953-2.24 1.746-3.785 1.638v.065c.434 1.026.864 2.053 1.318 3.074a1.65 1.65 0 0 0 .69.74a12 12 0 0 1-.183-1.283c-.024-.523.094-.617.617-.667a.17.17 0 0 1 .203.129c.074.187.163.37.246.558c.104-.227.193-.44.296-.642a.3.3 0 0 1 .149-.133c.222-.094.45-.168.686-.257l.177.356c.153-.35.296-.696.46-1.037a.375.375 0 0 1 .468-.173v.242a4.93 4.93 0 0 0 .493 2.626a4.2 4.2 0 0 1 .281 1.046c.04.162-.043.257-.196.257l-.4.044a1.7 1.7 0 0 1 .27.247c.075.094.179.27.144.32a.9.9 0 0 1-.39.267a.54.54 0 0 1-.315 0a10 10 0 0 1-.81-.272a4 4 0 0 1-.414-.212l-.08.094l.716.612a1.55 1.55 0 0 0 1.24.41a.987.987 0 0 0 .986-.988c.045-.306.005-.616.045-.922a2.7 2.7 0 0 1 .927-1.974a2.56 2.56 0 0 0-1.214.74a6.4 6.4 0 0 1-.079-1.135a1.545 1.545 0 0 1 1.402-1.653a21 21 0 0 1 1.648-.281c.805-.08 1.599-.246 2.369-.494a4.4 4.4 0 0 0 1.406-.794a18 18 0 0 1-2.872.38a8.88 8.88 0 0 0 3.4-4.777c-1.056 1.48-2.202 2.867-3.87 3.701a22.7 22.7 0 0 0 1.447-4.086zM5.443 8.07c-.03.153-.054.305-.094.454c-.074.29-.163.577-.237.868a.197.197 0 0 1-.198.178c-.384.045-.764.103-1.183.157zM9.4 10.775a4 4 0 0 0-.577.053a2.9 2.9 0 0 0-1.48.523a.99.99 0 0 0-.43.923a1.1 1.1 0 0 1 .972-.671a3.07 3.07 0 0 1 1.762.34c.49.234.772.76.696 1.298a2.34 2.34 0 0 1-.687 1.377a5.8 5.8 0 0 1-1.914 1.308a9.7 9.7 0 0 0-2.32 1.41a3.95 3.95 0 0 0-1.396 2.567h5.921a2.023 2.023 0 0 1 .987-2.551l-.26 1.115a5.43 5.43 0 0 0 1.243-2.665c.171.407.245.848.216 1.288a5.6 5.6 0 0 0 .44-2.866l.518.561c-.049-.4-.09-.755-.134-1.11a3.1 3.1 0 0 0-1.865-2.585a4.2 4.2 0 0 0-1.692-.315"/>
</svg>`
}]

onMounted(()=>{
  initDate();
  initLink();
  AuthorBlogs();
  getRecommendBlogs();
})

</script>

<template>
  <div class="grid grid-cols-[2fr,7fr] h-screen w-full" >
<!--    推荐-->
    <div   class="hidden-on-small-screens mt-24 flex flex-wrap rounded-xl h-fit px-8 ">
      <div class="items-center justify-center mx-auto  gap-6">
        <span class="font-kai text-2xl text-blue-900 ml-16 w-full ">精选文章</span>
        <div class="grid grid-cols-[1fr,7fr] gap-4 mt-4">
          <div class="flex flex-wrap gap-10 ">
            <div v-for="Suggestion in SuggestionBar" class="w-full">
              <div v-html="Suggestion.ico"></div>
            </div>
          </div>
          <div class="flex flex-wrap gap-8 ">
            <div v-for="recommendBlogs in options.recommendBlogs" >
              <div @click="blogClickHandle(recommendBlogs.id)" class="hc-transition hover:scale-105 active:scale-100 select-none cursor-pointer text-xl font-kai text-blue-500">{{recommendBlogs.title}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

<!--    Blogs栏-->
    <div class="blog-w-full flex flex-wrap overflow-y-auto   mt-14 p-10 gap-8 " >
<!--      搜索框-->
<!--     <div class="sticky z-10 top-0 w-full">-->
       <div class="items-center justify-center mx-auto flex flex-nowrap w-full">
         <input v-model="searchQuery.search" class="w-2/3 h-8 text-pink-400 font-kai text-xl border-blue-200 border-2 rounded-xl block focus:outline-none placeholder-pink-400 bg-transparent items-center justify-center my-auto  hover:shadow-outline hover:w-3/4 hover:h-10 transition-all duration-300 ease-in-out" placeholder="Your Content Here"/>
         <button @click="search">
           <svg xmlns="http://www.w3.org/2000/svg" width="128" height="128" viewBox="0 0 24 24" class="size-6">
             <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m21 21l-4.343-4.343m0 0A8 8 0 1 0 5.343 5.343a8 8 0 0 0 11.314 11.314"/>
           </svg>
         </button>
<!--       </div>-->
     </div>
<!--      gap只对flex/grid生效-->
      <div class="h-full flex flex-wrap gap-4 ">
        <div v-if="!IsSearch" class="min-w-full h-fit bg-opacity-5 shadow-xl select-none cursor-pointer hc-transition hover:scale-105 active:scale-100 rounded-xl p-1 mx-auto bg-gray-100" v-for="blog in options.blogs" @click="blogClickHandle(blog.blog.id)">
          <div class="flex flex-nowrap gap-4">
            <span class="text-blue-500 font-kai text-xl w-fit">{{blog.blog.title}}</span>
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 512 512" class="size-5 text-yellow-700 ">
              <path fill="currentColor" d="M511.974 271.891a47.74 47.74 0 0 0-14.706-33.008L311.57 57.98a29.9 29.9 0 0 0-21.2-8.731h-33.142l217.754 212.6l.092.088a15.907 15.907 0 0 1 .741 22.337l-156.4 173.355l21.953 21.356L499.447 305.85a47.75 47.75 0 0 0 12.527-33.959"/><path fill="currentColor" d="M426.9 283.161a23.92 23.92 0 0 0-5.231-24.742c-.106-.111-.2-.231-.306-.34L224.307 57.716l-.094-.094a31.8 31.8 0 0 0-22.628-9.373H60.194A44.244 44.244 0 0 0 16 92.443v141.1a32.12 32.12 0 0 0 10.045 23.28l210.32 200.2l.077.073a23.82 23.82 0 0 0 16.409 6.508q.447 0 .9-.017a24.1 24.1 0 0 0 6.741-1.217a23.85 23.85 0 0 0 10.047-6.517l151.444-164.621a24 24 0 0 0 4.917-8.071M252.5 428.2L48.077 233.612L48 233.54V92.443a12.21 12.21 0 0 1 12.194-12.194h141.39l191.7 194.918Z"/><path fill="currentColor" d="M140 112a52 52 0 1 0 52 52a52.06 52.06 0 0 0-52-52m0 72a20 20 0 1 1 20-20a20.023 20.023 0 0 1-20 20"/>
            </svg>
            <span class="text-black font-kai  w-fit">{{blog.blog.tag}}</span>
          </div>
          <div class=" grid grid-cols-[1fr,15fr]">
            <a-avatar shape="square" class="w-12 h-12 object-cover" :src="blog.avator">
            </a-avatar>
            <h2 class="font-kai text-blue-400">{{blog.blog.content}}</h2>
          </div>
          <h3 class="text-blue-500 font-kai text-xl mx-auto">{{blog.username}}</h3>
        </div>
        <div v-else class="h-fit bg-opacity-5 shadow-xl select-none cursor-pointer hc-transition hover:scale-105 active:scale-100 rounded-xl p-2 mx-auto bg-gray-100" v-for="blog in options.SearchBlogs" @click="blogClickHandle(blog.blog.id)">
          <div class="flex flex-nowrap gap-4">
            <span class="text-blue-500 font-kai text-xl w-fit">{{blog.blog.title}}</span>
            <svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 512 512" class="size-5 text-yellow-700 ">
              <path fill="currentColor" d="M511.974 271.891a47.74 47.74 0 0 0-14.706-33.008L311.57 57.98a29.9 29.9 0 0 0-21.2-8.731h-33.142l217.754 212.6l.092.088a15.907 15.907 0 0 1 .741 22.337l-156.4 173.355l21.953 21.356L499.447 305.85a47.75 47.75 0 0 0 12.527-33.959"/><path fill="currentColor" d="M426.9 283.161a23.92 23.92 0 0 0-5.231-24.742c-.106-.111-.2-.231-.306-.34L224.307 57.716l-.094-.094a31.8 31.8 0 0 0-22.628-9.373H60.194A44.244 44.244 0 0 0 16 92.443v141.1a32.12 32.12 0 0 0 10.045 23.28l210.32 200.2l.077.073a23.82 23.82 0 0 0 16.409 6.508q.447 0 .9-.017a24.1 24.1 0 0 0 6.741-1.217a23.85 23.85 0 0 0 10.047-6.517l151.444-164.621a24 24 0 0 0 4.917-8.071M252.5 428.2L48.077 233.612L48 233.54V92.443a12.21 12.21 0 0 1 12.194-12.194h141.39l191.7 194.918Z"/><path fill="currentColor" d="M140 112a52 52 0 1 0 52 52a52.06 52.06 0 0 0-52-52m0 72a20 20 0 1 1 20-20a20.023 20.023 0 0 1-20 20"/>
            </svg>
            <span class="text-black font-kai  w-fit">{{blog.blog.tag}}</span>
          </div>
          <div class="grid grid-cols-[1fr,15fr]">
            <a-avatar shape="square" class="w-2/3 h-fit" :src="blog.avator">
            </a-avatar>
            <h2 class="font-kai text-blue-400">{{blog.blog.content}}</h2>
          </div>
          <h3 class="text-blue-500 font-kai text-xl">{{blog.blog.username}}</h3>
        </div>
      </div>
    </div>

<!--    网络链接-->
<!--    <div class="grid grid-rows-[1fr,13fr] gap-4 ">-->
<!--      <div class=" font-kai flex flex-nowrap text-blue-300 mx-auto rounded-xl w-auto h-fit p-2 text-3xl cursor-pointer select-none bg-gray-100 dark:bg-gray-900 text-center">-->
<!--        <span>网页链接</span>-->
<!--        <button v-if="showForm===false" class="hc-transition rounded-full hover:scale-110 active:scale-90" @click="toggleForm">-->
<!--          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-10 text-blue-300 hover:text-red-500">-->
<!--            <path stroke-linecap="round" stroke-linejoin="round" d="m15 11.25-3-3m0 0-3 3m3-3v7.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />-->
<!--          </svg>-->
<!--        </button>-->
<!--        <button v-else class="hc-transition rounded-full hover:scale-110 active:scale-90" @click="toggleForm">-->
<!--          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-10 text-blue-300 hover:text-red-500">-->
<!--            <path stroke-linecap="round" stroke-linejoin="round" d="m11.25 9-3 3m0 0 3 3m-3-3h7.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />-->
<!--          </svg>-->
<!--        </button>-->
<!--      </div>-->
<!--      <div v-if="showForm">-->
<!--        <AddLinkForm />-->
<!--      </div>-->
<!--      <div v-else class=" bg-gray-100 dark:bg-gray-900 rounded-xl h-fit">-->
<!--        <div class="flex flex-wrap gap-4 ">-->
<!--          <div v-if="themeStore.currentTheme ==='light'"  class=" hc-transition w-11/12 hover:scale-105 active:scale-100  rounded-xl p-4 mx-auto bg-gray-100" v-for="links in option_link.links" >-->
<!--            <a :href="links.location" target="_blank" class="text-blue-300 hover:text-red-500">{{links.name}}</a>-->
<!--          </div>-->
<!--          <div v-else class=" w-11/12 hc-transition hover:scale-105 active:scale-100  rounded-xl p-4 mx-auto bg-gray-900" v-for="links in option_link.links" >-->
<!--            <a :href="links.location" target="_blank" class="hc-transition text-blue-300 hover:text-red-500">{{links.name}}</a>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->


  </div>
</template>

<style scoped>

@media (max-width: 1048px) {
  .hidden-on-small-screens {
    display: none;
  }
}

@media (max-width: 1048px) {
  .blog-w-full {
    grid-column: span 2; /* 让Blogs栏占满两列 */
  }
}


</style>