#!/bin/bash

# Change working directory to project root
cd ../..

# Create the main frontend directory
mkdir -p frontend/src/{lib/{components/{layout,user,event,ticket},services,stores},routes/{events/[id],login,register,tickets}} frontend/{static,tests}

# Create empty files
touch frontend/src/lib/components/layout/{Header,Footer}.svelte
touch frontend/src/lib/components/user/{LoginForm,RegisterForm}.svelte
touch frontend/src/lib/components/event/{EventList,EventDetails}.svelte
touch frontend/src/lib/components/ticket/{TicketBooking,TicketList}.svelte
touch frontend/src/lib/services/{api,auth}.js
touch frontend/src/lib/stores/{userStore,eventStore}.js
touch frontend/src/routes/{+layout,+page}.svelte
touch frontend/src/routes/{login,register,events,tickets}/+page.svelte
touch frontend/src/routes/events/[id]/+page.svelte
touch frontend/src/app.html
touch frontend/static/favicon.png
touch frontend/tests/test.js
touch frontend/{.gitignore,package.json,postcss.config.js,svelte.config.js,tailwind.config.js,vite.config.js}

echo "Frontend directory structure created successfully!"
