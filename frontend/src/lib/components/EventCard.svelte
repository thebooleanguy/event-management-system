<script>
  export let imageUrl = '';
  export let title = '';
</script>

<div class="bg-white rounded-lg overflow-hidden shadow-md">
  <img src={imageUrl} alt={title} class="w-full h-48 object-cover" />
  {#if title}
    <div class="p-4">
      <h3 class="text-lg font-semibold">{title}</h3>
    </div>
  {/if}
</div>
