<script setup>
import {onMounted, reactive, ref, watch} from "vue";
import {get} from "@/net/index.js";
import router from "@/router/index.js";
import { marked } from 'marked';
const counts=ref(0);
const expandedCards = ref(new Set());

function toggleExpand(blogId) {
  if (expandedCards.value.has(blogId)) {
    expandedCards.value.delete(blogId);
  } else {
    expandedCards.value.add(blogId);
  }
}

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

const renderMarkdown = (content) => {
  return marked(content);
}

// 生成简短描述
const generateDescription = (title) => {
  const descriptions = [
    "这是一篇关于" + title + "的精彩文章，点击展开阅读更多...",
    "探索" + title + "的奥秘，让我们一起深入了解...",
    "想要了解更多关于" + title + "的内容吗？点击展开查看详情...",
    "发现" + title + "的独特见解，精彩内容等你来读...",
    "深入了解" + title + "，开启你的知识之旅..."
  ];
  return descriptions[Math.floor(Math.random() * descriptions.length)];
}

onMounted(()=>{
  initDate();
  initLink();
  AuthorBlogs();
})

</script>

<template>
  <div class="main-container">
    <!-- 左侧博客列表 -->
    <div class="blog-list-container">
      <!-- 搜索框 -->
      <div class="search-container">
        <div class="search-wrapper">
          <input
            type="text"
            v-model="searchQuery.search"
            placeholder="搜索博客..."
            class="search-input border-white border-2 dark:bg-purple-200/10"
          />
          <button @click="search" class="search-button">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="search-icon">
              <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m21 21l-4.343-4.343m0 0A8 8 0 1 0 5.343 5.343a8 8 0 0 0 11.314 11.314"/>
            </svg>
          </button>
        </div>
      </div>

      <!-- 博客列表 -->
      <div class="blog-grid">
        <div v-if="!IsSearch"
             v-for="blog in options.blogs"
             :key="'blog-'+blog.blog.id"
             class="blog-card bg-white border-white border-2 dark:bg-purple-200/10 "
             @click="blogClickHandle(blog.blog.id)">
          <div class="blog-header">
            <div class="title-wrapper">
              <h2 class="blog-title dark:text-gray-200">{{blog.blog.title}}</h2>
              <div class="expand-button dark:text-purple-400 flex flex-nowrap w-14 my-auto" @click.stop="toggleExpand(blog.blog.id)">
                <span v-if="expandedCards.has(blog.blog.id)" class="my-auto">收起</span>
                <span v-else class="my-auto">展开</span>
                <svg xmlns="http://www.w3.org/2000/svg"
                     :class="{ 'rotate-180': expandedCards.has(blog.blog.id) }"
                     class="expand-icon size-6 my-auto"
                     viewBox="0 0 24 24">
                  <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 9l-7 7l-7-7"/>
                </svg>
              </div>
            </div>
            <span class="blog-tag dark:bg-purple-500/20 rounded-xl p-2 dark:text-purple-400">{{blog.blog.tag}}</span>
          </div>

          <div class="blog-content" @click.stop>
            <div class="blog-description  dark:text-gray-300"
                 :class="{ 'expanded': expandedCards.has(blog.blog.id) }">
              <template v-if="expandedCards.has(blog.blog.id)">
                <div v-html="renderMarkdown(blog.blog.content)"></div>
              </template>
<!--              <template v-else>-->
<!--                <div>{{ generateDescription(blog.blog.title) }}</div>-->
<!--              </template>-->
            </div>
          </div>

          <div class="blog-footer">
            <div class="author-info dark:bg-purple-500/10">
              <img :src="blog.avator" :alt="blog.username" class="author-avatar dark:border-purple-500" />
              <span class="author-name dark:text-gray-300">{{blog.username}}</span>
            </div>
            <div class="blog-meta">
              <span class="publish-date dark:text-gray-400">2024-03-21</span>
            </div>
          </div>
        </div>

        <div v-else
             v-for="blog in options.SearchBlogs"
             :key="'search-'+blog.blog.id"
             class="blog-card bg-white dark:bg-purple-200/10"
             @click="blogClickHandle(blog.blog.id)">
          <div class="blog-header">
            <div class="title-wrapper">
              <h2 class="blog-title dark:text-gray-200">{{blog.blog.title}}</h2>
              <div class="expand-button dark:text-purple-400" @click.stop="toggleExpand(blog.blog.id)">
                <p v-if="expandedCards.has(blog.blog.id)">收起</p>
                <p v-else>展开</p>
                <svg xmlns="http://www.w3.org/2000/svg"
                     :class="{ 'rotate-180': expandedCards.has(blog.blog.id) }"
                     class="expand-icon"
                     viewBox="0 0 24 24">
                  <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m19 9l-7 7l-7-7"/>
                </svg>
              </div>
            </div>
            <span class="blog-tag dark:bg-purple-500/20 dark:text-purple-400">{{blog.blog.tag}}</span>
          </div>

          <div class="blog-content" @click.stop>
            <div class="blog-description dark:bg-purple-200/10 dark:text-gray-300"
                 :class="{ 'expanded': expandedCards.has(blog.blog.id) }">
              <template v-if="expandedCards.has(blog.blog.id)">
                <div v-html="renderMarkdown(blog.blog.content)"></div>
              </template>
              <template v-else>
                <div>{{ generateDescription(blog.blog.title) }}</div>
              </template>
            </div>
          </div>

          <div class="blog-footer">
            <div class="author-info dark:bg-purple-500/10">
              <img :src="blog.avator" :alt="blog.blog.username" class="author-avatar dark:border-purple-500" />
              <span class="author-name dark:text-gray-300">{{blog.blog.username}}</span>
            </div>
            <div class="blog-meta">
              <span class="publish-date dark:text-gray-400">2024-03-21</span>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  padding: 2rem;
  padding-left: calc(2rem + 250px);
  min-height: 100vh;
}

.blog-list-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.search-container {
  position: sticky;
  top: 1rem;
  z-index: 10;
}

.search-wrapper {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 100%;
  padding: 1rem 3rem 1rem 1.5rem;
  border-radius: 1rem;
  font-size: 1rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

.search-input:focus {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.dark .search-input {
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2);
}

.dark .search-input:focus {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.3), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
}

.search-button {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  padding: 0.5rem;
  transition: all 0.3s ease;
}

.search-icon {
  width: 1.5rem;
  height: 1.5rem;
}

.blog-card {
  border-radius: 1.5rem;
  padding: 1.5rem;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin-bottom: 1.5rem;
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
  animation: cardAppear 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.1);
  background: white;
}

@keyframes cardAppear {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.blog-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, rgba(87, 17, 152, 0.05) 0%, rgba(153, 72, 234, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.blog-card:hover {
  transform: translateX(5px) scale(1.02);
  box-shadow: 0 4px 6px -1px rgba(219, 58, 237, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.blog-card:hover::before {
  opacity: 1;
}

.dark .blog-card {
  background: rgba(70, 34, 88, 0.2);
}

.dark .blog-card::before {
  background: linear-gradient(45deg, rgba(192, 132, 252, 0.05) 0%, rgba(147, 51, 234, 0.05) 100%);
}

.blog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  position: relative;
  overflow: hidden;
}

.title-wrapper {
  display: flex;
  align-items: center;
  gap: 1rem;
  position: relative;
}

.blog-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
  color: #333;
}

.dark .blog-title {
  color: #e2e8f0;
}

.blog-title::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: -2px;
  left: 0;
  background: linear-gradient(90deg, rgba(147, 51, 234, 0.8), rgba(192, 132, 252, 0.8));
  transition: width 0.3s ease;
}

.blog-card:hover .blog-title::after {
  width: 100%;
}

.dark .blog-title::after {
  background: linear-gradient(90deg, rgba(192, 132, 252, 0.8), rgba(147, 51, 234, 0.8));
}

.blog-content {
  position: relative;
  margin: 0.75rem 0;
  padding: 0 0.5rem;
  transition: all 0.3s ease;
  color: #4a5568;
}

.dark .blog-content {
  color: #cbd5e0;
}

.blog-description {
  line-height: 1.8;
  margin: 0;
  max-height: 0;
  overflow: hidden;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  font-size: 1rem;
  padding: 0.5rem 0;
}

.blog-description:not(.expanded) {
  max-height: 100px;
  padding: 1rem;
  border-radius: 0.5rem;
  animation: descriptionAppear 0.5s ease-out forwards;
}

.blog-description.expanded {
  max-height: 2000px;
  padding: 1rem;
  border-radius: 0.5rem;
  animation: contentExpand 0.5s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

@keyframes contentExpand {
  0% {
    opacity: 0;
    transform: translateY(10px) scale(0.98);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.blog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
  padding-top: 1rem;
  position: relative;
  overflow: hidden;
  color: #4a5568;
}

.dark .blog-footer {
  color: #cbd5e0;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 1rem;
  border-radius: 9999px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.author-avatar {
  width: 2.25rem;
  height: 2.25rem;
  border-radius: 9999px;
  object-fit: cover;
  transition: all 0.3s ease;
  animation: avatarAppear 0.5s ease-out forwards;
  border: 2px solid transparent;
  box-shadow: 0 0 0 0 rgba(147, 51, 234, 0.4);
}

@keyframes avatarAppear {
  from {
    transform: scale(0.8);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.author-avatar:hover {
  transform: scale(1.1) rotate(5deg);
  border-color: rgba(147, 51, 234, 0.5);
  box-shadow: 0 0 0 4px rgba(147, 51, 234, 0.2);
  animation: avatarPulse 1.5s infinite;
}

@keyframes avatarPulse {
  0% {
    box-shadow: 0 0 0 0 rgba(147, 51, 234, 0.4);
  }
  70% {
    box-shadow: 0 0 0 6px rgba(147, 51, 234, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(147, 51, 234, 0);
  }
}

.dark .author-avatar {
  box-shadow: 0 0 0 0 rgba(192, 132, 252, 0.4);
}

.dark .author-avatar:hover {
  border-color: rgba(192, 132, 252, 0.5);
  box-shadow: 0 0 0 4px rgba(192, 132, 252, 0.2);
  animation: avatarPulseDark 1.5s infinite;
}

@keyframes avatarPulseDark {
  0% {
    box-shadow: 0 0 0 0 rgba(192, 132, 252, 0.4);
  }
  70% {
    box-shadow: 0 0 0 6px rgba(192, 132, 252, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(192, 132, 252, 0);
  }
}

.publish-date {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  position: relative;
  padding: 0.25rem 0.5rem;
  border-radius: 0.5rem;
  transition: all 0.3s ease;
}



/* 响应式布局 */
@media (max-width: 1024px) {
  .main-container {
    padding-left: 2rem;
  }
}

@media (max-width: 768px) {
  .main-container {
    padding: 1rem;
    padding-left: 1rem;
  }
}
</style>